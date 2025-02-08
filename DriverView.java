import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverView extends JFrame {
    
    private JTextField infoField;
    private JTextArea outputArea;
    private JButton cal;
    private JButton testButton;
    private JButton writtenTestButton;
    private JButton practicalTestButton;
    private JButton trainingButton;
    private DriverController controller;
    private boolean isTesting = false;
    
    private JLabel reportLabel;

    public DriverView(DriverController controller) {
        this.controller = controller;

        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel textLabel = new JLabel("หมายเลขใบขับขี่:");
        textLabel.setBounds(20, 40, 120, 25);
        add(textLabel);

        infoField = new JTextField();
        infoField.setBounds(150, 40, 260, 25);
        add(infoField);

        outputArea = new JTextArea();
        outputArea.setBounds(20, 90, 430, 80);
        outputArea.setEditable(false);
        add(outputArea);

        cal = new JButton("ตรวจสอบ");
        cal.setBounds(420, 40, 120, 25);
        add(cal);

        testButton = new JButton("ทดสอบสมรรถนะ");
        testButton.setBounds(20, 180, 160, 30);
        testButton.setVisible(false);
        add(testButton);

        writtenTestButton = new JButton("สอบข้อเขียน");
        writtenTestButton.setBounds(20, 180, 160, 30);
        writtenTestButton.setVisible(false);
        add(writtenTestButton);

        practicalTestButton = new JButton("สอบปฏิบัติ");
        practicalTestButton.setBounds(20, 220, 160, 30);
        practicalTestButton.setVisible(false);
        add(practicalTestButton);

        trainingButton = new JButton("อบรม");
        trainingButton.setBounds(20, 260, 160, 30);
        trainingButton.setVisible(false);
        add(trainingButton);

        reportLabel = new JLabel();
        reportLabel.setBounds(550, 40, 220, 150);
        add(reportLabel);

        String report = controller.getDriverReport();
        reportLabel.setText(report);

        cal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String licenseNumber = infoField.getText().trim();
                String result = controller.checkLicense(licenseNumber);
                outputArea.setText(result);

                testButton.setVisible(false);
                writtenTestButton.setVisible(false);
                practicalTestButton.setVisible(false);
                trainingButton.setVisible(false);

                int age = extractAge(result);
                if (result.contains("มือใหม่")) {
                    if (age >= 16 && age <= 50) {
                        writtenTestButton.setVisible(true);
                        practicalTestButton.setVisible(true);
                    }
                } else if (result.contains("คนขับรถสาธารณะ")) {
                    if (age >= 20 && age <= 60) {
                        if (result.contains("ถูกระงับชั่วคราว")) {
                            trainingButton.setVisible(true);
                        }
                        testButton.setVisible(true);
                    }
                } else if (result.contains("บุคคลทั่วไป")) {
                    if (age >= 16 && age <= 70) {
                        testButton.setVisible(true);
                    }
                }
                
                String report = controller.getDriverReport();
                reportLabel.setText(report);
            }
        });

        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTesting) {
                    outputArea.setText("สิ้นสุดการทดสอบสมรรถนะ");
                    testButton.setText("ทดสอบสมรรถนะ");
                } else {
                    outputArea.setText("เริ่มการทดสอบสมรรถนะ...");
                    testButton.setText("สิ้นสุดการทดสอบ");
                }
                isTesting = !isTesting;
            }
        });

        setVisible(true);
    }

    private int extractAge(String text) {
        Pattern pattern = Pattern.compile("อายุ: (\\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        DriverController controller = new DriverController();
        new DriverView(controller);
    }
}
