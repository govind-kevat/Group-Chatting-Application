package group.chatting.application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserTwo implements ActionListener, Runnable {

    JTextField text;
    JPanel a1;
    JScrollPane scroll;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();

    BufferedWriter writer;
    BufferedReader reader;
    String name = "Govind Kevat";

    UserTwo() {
        f.setLayout(null);

        a1 = new JPanel();
        a1.setLayout(new BorderLayout());
        a1.add(vertical, BorderLayout.PAGE_START);

        scroll = new JScrollPane(a1);
        scroll.setBounds(5, 75, 440, 520);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        f.add(scroll);

        text = new JTextField();
        text.setBounds(5, 605, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320, 605, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        text.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        JLabel back = new JLabel(loadIcon("3.png", 25, 25));
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });


        JLabel profile = new JLabel(loadIcon("1.jpeg", 50, 50));
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        JLabel video = new JLabel(loadIcon("video.png", 30, 30));
        video.setBounds(290, 20, 30, 30);
        p1.add(video);

        JLabel phone = new JLabel(loadIcon("phone.png", 35, 30));
        phone.setBounds(350, 20, 35, 30);
        p1.add(phone);

        JLabel menu = new JLabel(loadIcon("3icon.png", 10, 25));
        menu.setBounds(410, 20, 10, 25);
        p1.add(menu);

        JLabel groupName = new JLabel("Minor Project");
        groupName.setBounds(110, 15, 100, 18);
        groupName.setForeground(Color.WHITE);
        groupName.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(groupName);

        JLabel status = new JLabel("Govind,jayant,mukesh");
        status.setBounds(110, 35, 160, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        f.setSize(465, 700);
        f.setLocation(20, 50);
        f.getContentPane().setBackground(Color.WHITE);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        try {
            Socket socket = new Socket("localhost", 2003);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ImageIcon loadIcon(String file, int w, int h) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/" + file));
        Image scaled = icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scroll.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String msg = text.getText();
            if (msg.isEmpty()) return;

            String out = name + ": " + msg;

            JPanel p2 = formatLabel(out);
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            writer.write(out);
            writer.write("\r\n");
            writer.flush();

            text.setText("");
            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.startsWith(name + ":")) continue;

                JPanel panel = formatLabel(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));

                f.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserTwo two = new UserTwo();
        Thread t1 = new Thread(two);
        t1.start();
    }
}