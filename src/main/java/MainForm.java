import FindI.*;
import graphicForm.graphicJDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;


public class MainForm extends JDialog {
    private JPanel contentPane;
    private JTextField fileName;
    private JButton chooseFileButton;
    private JButton graphicButton;
    private JPanel graphicPanel;
    private JButton peaksButton;
    private JButton buttonCancel;
    File fileWithDataName = null;
    Vector<Double> x = new Vector<>();
    Vector<Double> y = new Vector<>();
    //Graphics2D g2D;

    public MainForm() {
        //setVisible(false);
        //setSize(600,600);
        setLocationRelativeTo(null);
        //contentPane.setSize(500,500);
        setContentPane(contentPane);
        setModal(true);
        //graphicPanel.setSize(10000,10000);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getRootPane().setDefaultButton(chooseFileButton);
        setModal(true);
        addListeners();
    }
    public class MyGraphics extends JComponent {

        private static final long serialVersionUID = 1L;

        MyGraphics() {
            //setPreferredSize(new Dimension(500, 100));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillRect(200, 62, 30, 10);
        }
    }
   // public void paint(Graphics g) {
      //  this.g2D = (Graphics2D) g;
  //  }

    private void addListeners() {
        chooseFileButton.setText("choose file");
        chooseFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileWithDataName = chooseFileButtonPressed();
            }
        });
        peaksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findIMax f = new findIMax(x,y);
            }
        });
        graphicButton.setText("graphic");
        graphicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFileData();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    /*
    private void draw() {
        double lastX = 0, lastY = 0;

        g2D.setColor(Color.BLUE);

        for (int i = 0; i < x.size(); i++) {
            g2D.drawLine((int) (lastX * 10), 600 - (int) (lastY * 10),
                    600 - (int) (x.get(i) * 10), (int) (y.get(i) * 10));

            lastX = x.get(i);
            lastY = y.get(i);
        }
    }*/

    private void getFileData() {
        //if (fileWithDataName != null) {
        try {
            //FileReader fr = new FileReader(fileWithDataName); //TODO исправить на получение файла из указанной директории
            FileReader fr = new FileReader("src/main/java/TS-1P.dat");
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                // System.out.println(line);
                //String[] splitters = new String[]{" ", "\t"};
                var lines = line.split("\t");
                //System.out.println(lines[0]);
                x.add(Double.parseDouble(lines[1]));
                y.add(Double.parseDouble(lines[0]));
                line = reader.readLine();
            }
            //System.out.println(this);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    var secform = new graphicJDialog(x,y);
                    secform.setSize(300,300);
                    //secform.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    secform.setVisible(true);
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //}
    }

    private File chooseFileButtonPressed() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Выбор директории");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this,
                    jFileChooser.getSelectedFile());
        }
        return jFileChooser.getSelectedFile();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MainForm dialog = new MainForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
