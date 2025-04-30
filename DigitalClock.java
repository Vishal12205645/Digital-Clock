import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DigitalClock extends JFrame {

    private JLabel timeLabel;
    private JButton startStopButton, saveButton;
    private boolean running = true;
    private DateTimeFormatter timeFormatter;

    public DigitalClock() {
        setTitle("Digital Clock");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        add(timeLabel);

        startStopButton = new JButton("Stop");
        add(startStopButton);

        saveButton = new JButton("Save Time");
        add(saveButton);

      
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = !running;
                startStopButton.setText(running ? "Stop" : "Start");
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTimeToFile();
            }
        });

        new Thread(new TimeUpdater()).start();
    }

    
    private class TimeUpdater implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (running) {
                    LocalTime now = LocalTime.now();
                    String time = now.format(timeFormatter);
                    timeLabel.setText(time);
                }
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveTimeToFile() {
        String currentTime = timeLabel.getText();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saved_time.txt", true))) {
            writer.write(currentTime);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Time saved to file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving time: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DigitalClock().setVisible(true);
        });
    }
}
