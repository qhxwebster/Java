import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

class Tile{
    int x;
    int y;

    public Tile(int x0,int y0){
        x = x0;
        y = y0;
    }
}

public class SnakeDemo extends JComponent{

    private JLabel label = new JLabel("当前长度：");
    private JLabel label2 = new JLabel("所花时间：");
    private JLabel Score = new JLabel("1");
    private JLabel Time = new JLabel("");
    private Font f = new Font("微软雅黑",Font.PLAIN,15);
    private JPanel p = new JPanel();
    private JComboBox box = new JComboBox();
    private String[] choice = {"很慢", "慢", "一般", "快", "较快", "皮人专用","上一个爽吗？"};

    private final int MAX_SIZE = 400;//蛇身体最长为400节
    private Tile temp = new Tile(0,0);
    private Tile temp2 = new Tile(0,0);
    private Tile head;
    private Tile[] body = new Tile[MAX_SIZE];

    private String direction = "R";//默认向右走
    private String current_direction = "R";//当前方向
    private boolean first_launch = false;
    private boolean iseaten = false;
    private boolean isrun = true;
    private int randomx,randomy;
    private int body_length = 0;//身体长度初始化为0
    private Thread run;

    private int hour =0;
    private int min =0;
    private int sec =0 ;

    private boolean pause = false;

    private long millis = 250;

    public SnakeDemo(){


        String lookAndFeel =UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        }

        //布局
        add(label);
        label.setBounds(500, 10, 200, 20);
        label.setFont(f);
        add(Score);
        Score.setBounds(500, 35, 200, 20);
        Score.setFont(f);
        add(label2);
        label2.setBounds(500, 60, 200, 20);
        label2.setFont(f);
        add(Time);
        Time.setBounds(500, 85, 200, 20);
        Time.setFont(f);
        JPanel panel = new JPanel();
        add(panel);
        panel.setBounds(500, 100, 100, 400);
        panel.setLayout(new FlowLayout());
        for(int i = 0 ; i < choice.length; i++)
            box.addItem(choice[i]);
        box.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                millis = (5 - box.getSelectedIndex()) * 100;
                if(box.getSelectedIndex() == 5)
                    millis = 10;
                box.setFocusable(false);
                shutDown(true);
            }
        });
        box.setVisible(true);
        box.setSize(100, getHeight());
        //panel.setBackground(Color.black);
        panel.add("Center", box);
        //初始化头部坐标
        ProduceRandom();
        head = new Tile(randomx,randomy);

        //初始化身体所有节点
        for(int i = 0; i < MAX_SIZE;i++)
        {
            body[i] = new Tile(0,0);
        }

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    if(isrun && current_direction != "L")
                    {
                        direction = "R";
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    if(isrun && current_direction != "R")
                    {
                        direction = "L";
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_UP)
                {
                    if(isrun && current_direction != "D")
                    {
                        direction = "U";
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    if(isrun && current_direction != "U")
                    {
                        direction = "D";
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)//重置所有变量初始值
                {
                    //初始化头部坐标
                    ProduceRandom();
                    head = new Tile(randomx,randomy);
                    //初始化身体节点部坐标
                    for(int i = 0; i < MAX_SIZE;i++)
                    {
                        body[i].x = 0;
                        body[i].y = 0;
                    }

                    hour =0;
                    min =0;
                    sec =0 ;
                    direction = "R";//默认向右走
                    current_direction = "R";//当前方向
                    first_launch = false;
                    iseaten = false;
                    isrun = true;
                    pause = false;
                    millis = 500;//每隔500ms刷新一次
                    body_length = 0;
                    Score.setText("1");

                    run = new Thread();
                    run.start();
                    System.out.println("Start again");
                }

                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    if(!pause)//暂停
                    {
                        pause = true;
                        isrun = false;
                    }
                    else//开始
                    {
                        pause = false;
                        isrun = true;
                    }
                }
            }
        });

        //开始计时
        new Timer();

        setFocusable(true);
    }

    public void paintComponent(Graphics g1){
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);

        //画头部
        g.setColor(Color.red);
        g.fillOval(head.x, head.y, 20, 20);

        g.setPaint(new GradientPaint(115,135,Color.CYAN,230,135,Color.MAGENTA,true));
        if(!first_launch)
        {
            //初始化身体
            int x = head.x;
            for(int i = 0;i < body_length;i++)
            {
                x -= 22;//相邻两个方块的间距为2个像素，方块宽度都为20像素
                body[i].x = x;
                body[i].y = head.y;
                g.fillRoundRect(body[i].x, body[i].y, 20, 20, 10, 10);
            }
            //初始化食物位置
            ProduceRandom();
            g.fillOval(randomx, randomy, 19, 19);
        }
        else
        {
            //每次刷新身体
            for(int i = 0;i < body_length;i++)
            {
                g.fillRoundRect(body[i].x, body[i].y, 20, 20, 10, 10);
            }

            if(EatFood())//被吃了重新产生食物
            {
                ProduceRandom();
                g.fillOval(randomx, randomy, 19, 19);
                iseaten = false;
                if(millis >= 200)
                    millis -= 20;
            }
            else
            {
                g.fillOval(randomx, randomy, 19, 19);
            }
        }
        first_launch = true;

        //墙
        g.setStroke( new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
        g.setBackground(Color.black);
        g.drawRect(2, 7, 491, 469);

        //网格线
        for(int i = 1;i < 22;i++)
        {
            g.setStroke( new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
            g.setColor(Color.black);
            g.drawLine(5+i*22,9,5+i*22,472);
            if(i <= 20)
            {
                g.drawLine(4,10+i*22,491,10+i*22);
            }
        }
    }

    public void ProduceRandom(){
        boolean flag = true;
        Random rand = new Random();
        randomx = (rand.nextInt(21) + 1) * 22 + 7;
        randomy = (rand.nextInt(20) + 1) *22 + 12;
        while(flag)
        {
            if(body_length == 0)
            {
                flag = false;
            }
            else
            {
                for(int i = 0;i < body_length; i++)
                {
                    if(body[i].x == randomx && body[i].y == randomy)
                    {
                        randomx = (rand.nextInt(21) + 1) * 22 + 7;
                        randomy = (rand.nextInt(20) + 1) *22 + 12;
                        flag = true;
                        break;
                    }
                    else
                    {
                        if(i == body_length - 1)
                        {
                            flag = false;
                        }
                    }
                }
            }
        }
    }


    public void shutDown(boolean judge){

        new AePlayWave("over.wav").start();
        isrun = false;
        int result;
        if(judge) {
            result = JOptionPane.YES_OPTION;
        }else {
            result = JOptionPane.showConfirmDialog(null, "Game over! Try again?", "Information", JOptionPane.YES_NO_OPTION);
        }
        if(result == JOptionPane.YES_OPTION)
        {
            //初始化头部坐标
            ProduceRandom();
            head = new Tile(randomx,randomy);
            //初始化身体节点部坐标
            for(int i = 0; i < MAX_SIZE;i++)
            {
                body[i].x = 0;
                body[i].y = 0;
            }

            hour =0;
            min =0;
            sec =0 ;
            direction = "R";//默认向右走
            current_direction = "R";//当前方向
            first_launch = false;
            iseaten = false;
            isrun = true;
            pause = false;
            body_length = 0;
            Score.setText("1");

            run = new Thread();
            run.start();
            System.out.println("Start again");
        }
        else
        {
            System.exit(0);
//            run.stop();
        }
    }
    public void HitWall(){
        if((current_direction == "L" && head.x<7) || (current_direction == "R" && head.x>489)
                || (current_direction == "U" && head.y<12) || (current_direction == "D" && head.y>472)){
            shutDown(false);
        }
    }
    public void HitSelf(){//判断是否撞到自己身上
        for(int i = 0;i < body_length; i++)
        {
            if(body[i].x == head.x && body[i].y == head.y)
            {
                shutDown(false);
                break;
            }
        }
    }

    public boolean  EatFood(){
        if(head.x == randomx && head.y == randomy)
        {
            iseaten = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void Thread(){
        run = new Thread() {
            public void run() {
                while (true)
                {
                    try {
                        Thread.sleep(millis);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    if(!pause)
                    {
                        temp.x = head.x;
                        temp.y = head.y;
                        //头部移动
                        if(direction == "L")
                        {
                            head.x -= 22;
                        }
                        if(direction == "R")
                        {
                            head.x += 22;
                        }
                        if(direction == "U")
                        {
                            head.y -= 22;
                        }
                        if(direction == "D")
                        {
                            head.y += 22;
                        }
                        current_direction = direction;//刷新当前前进方向
                        //身体移动
                        for(int i = 0;i < body_length;i++)
                        {
                            temp2.x = body[i].x;
                            temp2.y = body[i].y;
                            body[i].x = temp.x;
                            body[i].y = temp.y;
                            temp.x = temp2.x;
                            temp.y = temp2.y;
                        }

                        if(EatFood())
                        {
                            body_length ++;
                            body[body_length-1].x = temp.x;
                            body[body_length-1].y = temp.y;
                            Score.setText("" + (body_length+1) );
                            new AePlayWave("eat.wav").start();
                        }

                        repaint();
                        //刷新完判断是否撞墙和撞自己的身体
                        HitWall();
                        HitSelf();
                    }
                }
            }
        };

        run.start();
    }

    public static void main(String[] args) {
        SnakeDemo snake = new SnakeDemo();
        snake.Thread();

        JFrame game = new JFrame();
        Image img = Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
        game.setIconImage(img);
        game.setTitle("Snake By Webster");
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setSize(600, 520);
        game.setResizable(false);
        game.setLocationRelativeTo(null);

        game.add(snake);
        System.out.println("OK");
        game.setVisible(true);
    }

    //计时器类
    class Timer extends Thread{
        public Timer(){
            this.start();
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(true){
                if(isrun){
                    sec +=1 ;
                    if(sec >= 60){
                        sec = 0;
                        min +=1 ;
                    }
                    if(min>=60){
                        min=0;
                        hour+=1;
                    }
                    showTime();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        private void showTime(){
            String strTime ="" ;
            if(hour < 10)
                strTime = "0"+hour+":";
            else
                strTime = ""+hour+":";

            if(min < 10)
                strTime = strTime+"0"+min+":";
            else
                strTime =strTime+ ""+min+":";

            if(sec < 10)
                strTime = strTime+"0"+sec;
            else
                strTime = strTime+""+sec;

            //在窗体上设置显示时间
            Time.setText(strTime);
        }
    }
}

class AePlayWave extends Thread {
    private String filename;
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb 

    public AePlayWave(String wavfile) {
        filename = wavfile;
    }

    public void run() {
        File soundFile = new File(filename);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            auline.drain();
            auline.close();
        }
    }
}