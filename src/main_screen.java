import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;



public class main_screen {


    private JPanel GHackTool;
    private JTabbedPane tabbedPane1;
    private JTextField all_words;
    private JButton searchButton;
    private JPanel ags;
    private JPanel ghdb;
    private JPanel exp;
    private JTextField exact_word;
    private JTextField any_words;
    private JTextField non_words;
    private JTextField num_from;
    private JTextField num_to;
    private JComboBox terms_in;
    private JComboBox file_types;
    private JTextField type;
    private JComboBox lang_box;
    private JComboBox regions_box;
    private JTextField site_field;
    private JRadioButton cache;
    private JComboBox last_update;
    private JTable table1;
    private JButton search_dork;
    private JLabel ghdb_label;
    private JButton exploit;
    private JTable table2;
    private JLabel exploit_label;
    public SearchOperators searchOperators;
    public String url="http://www.google.com/search?q=";



    public main_screen() {
        ghdb();
        fill_combos();
        exploit();

        searchOperators=new SearchOperators();

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                url=create_link(url);
                if (Desktop.isDesktopSupported()) {
                    // Windows
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // Ubuntu
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        runtime.exec("/usr/bin/firefox -new-window " + url);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                url="http://www.google.com/search?q=";
            }
        });


        file_types.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(!file_types.getSelectedItem().toString().equals("Other"))
                {
                    type.setEnabled(false);
                }
                else
                {
                    type.setEnabled(true);
                }

            }
        });


        search_dork.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRowIndex = table1.getSelectedRow();
                String selectedObject = (String) table1.getModel().getValueAt(selectedRowIndex, 1);
                String url_dork=selectedObject;
                if (Desktop.isDesktopSupported()) {
                    // Windows
                    try {
                        Desktop.getDesktop().browse(new URI(url_dork));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // Ubuntu
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        runtime.exec("/usr/bin/firefox -new-window " + url_dork);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        exploit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRowIndex = table2.getSelectedRow();
                String selectedObject = (String) table2.getModel().getValueAt(selectedRowIndex, 1);
                String url_exploit=selectedObject;
                if (Desktop.isDesktopSupported()) {
                    // Windows
                    try {
                        Desktop.getDesktop().browse(new URI(url_exploit));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // Ubuntu
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        runtime.exec("/usr/bin/firefox -new-window " + url_exploit);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GHackTool");
        frame.setContentPane(new main_screen().GHackTool);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public String create_link(String url)
    {
        if(cache.isSelected()==false)
        {
            if(!site_field.getText().equals(""))
                url+="site:"+site_field.getText()+"+";
        }
        else
            url+="cache:"+site_field.getText()+"+";


        String value=String.valueOf(terms_in.getSelectedItem());
        if(value.equals("In the url of the page"))
            url+="inurl:";
        else if(value.equals("In the title of the page"))
            url+="intitle:";
        else if(value.equals("In the text of the page"))
            url+="intext:";
        else if(value.equals("In links to the page"))
            url+="link:";


        if(!all_words.getText().equals(""))
        {
            String[] words=all_words.getText().split(",");
            for(int i=0;i<words.length;i++)
            {
                words[i]=words[i].replaceAll(" ","+");
            }
            url=searchOperators.all_the_words(url,words);
        }

        if(!exact_word.getText().equals(""))
        {
            String[] words=exact_word.getText().split(",");
            for(int i=0;i<words.length;i++)
            {
                words[i]=words[i].replaceAll(" ","+");
            }
            url=searchOperators.exact_phrase(url,words);
        }

        if(!any_words.getText().equals(""))
        {

            String[] words=any_words.getText().split(",");
            for(int i=0;i<words.length;i++)
            {
                words[i]=words[i].replaceAll(" ","+");
            }
            url=searchOperators.any_of_words(url,words);
        }

        if(!non_words.getText().equals(""))
        {
            String[] words=non_words.getText().split(",");
            for(int i=0;i<words.length;i++)
            {
                words[i]=words[i].replaceAll(" ","+");
            }
            url=searchOperators.non_of_words(url,words);

        }

        if(!num_from.getText().equals("") && !num_to.getText().equals(""))
        {
            String number1=num_from.getText();
            String number2=num_to.getText();
            url=searchOperators.range(url,number1,number2);

        }

        if(!type.getText().equals(""))
            url+="filetype:"+type.getText()+"+";
        else
        {

            String value1=String.valueOf(file_types.getSelectedItem());
            if(value1.equals("Adobe Flash (.swf)"))
                url+="filetype:swf+";
            else if(value1.equals("Adobe Portable Document Format (.pdf)"))
                url+="filetype:pdf+";
            else if(value1.equals("HTML (.html)"))
                url+="filetype:html+";
            else if(value1.equals("Microsoft Excel (.xlsx)"))
                url+="filetype:xlsx+";
            else if(value1.equals("Microsoft PowerPoint (.pptx)"))
                url+="filetype:pptx+";
            else if(value1.equals("Microsoft Word (.docx)"))
                url+="filetype:docx+";
            else if(value1.equals("OpenOffice presentation (.odp)"))
                url+="filetype:odp+";
            else if(value1.equals("OpenOffice text (.odt)}"))
                url+="filetype:odt+";
            else if(value1.equals("Text (.txt)"))
                url+="filetype:txt+";
            else if(value1.equals("C source code (.c)"))
                url+="filetype:c+";
            else if(value1.equals("C++ source code (.cpp)"))
                url+="filetype:ccp+";
            else if(value1.equals("C# source code (.cs)"))
                url+="filetype:cs+";
            else if(value1.equals("Java source code (.java)"))
                url+="filetype:java+";
            else if(value1.equals("Python source code (.py)"))
                url+="filetype:py+";
            else if(value1.equals("Wireless Markup Language (.wml)"))
                url+="filetype:wml";
            else if(value1.equals("XML (.xml)"))
                url+="filetype:xml+";

            String value2=String.valueOf(last_update.getSelectedItem());
            if(!value2.equals("Anytime"))
            {
                if(value2.equals("Last 1 Hour"))
                    url+="&tbs=qdr:h";
                else if(value2.equals("Last 24 Hour"))
                    url+="&tbs=qdr:d";
                else if(value2.equals("Last Week"))
                    url+="&tbs=qdr:w";
                else if(value2.equals("Last Month"))
                    url+="&tbs=qdr:m";
                else if(value2.equals("Last Year"))
                    url+="&tbs=qdr:y";
            }
            else
                url+="&tbas=0";

            String value3=String.valueOf(lang_box.getSelectedItem());
            if(!value3.equals("Any Language"))
            {
                if(value3.equals("English"))
                    url+="&lr=lang_en";
                else if(value3.equals("Turkish"))
                    url+="&lr=lang_tr";
                else if(value3.equals("German"))
                    url+="&lr=lang_de";
                else if(value3.equals("French"))
                    url+="&lr=lang_fr";
                else if(value3.equals("Italian"))
                    url+="&lr=lang_it";
                else if(value3.equals("Spanish"))
                    url+="&lr=lang_es";
                else if(value3.equals("Russian"))
                    url+="&lr=lang_ru";
                else if(value3.equals("Polish"))
                    url+="&lr=lang_pl";
                else if(value3.equals("SLovenian"))
                    url+="&lr=lang_sl";
                else if(value3.equals("Crotian"))
                    url+="&lr=lang_hr";
            }

            String value4=String.valueOf(regions_box.getSelectedItem());
            if(!value4.equals("Any Region"))
            {
                if(value4.equals("ABD"))
                    url+="&cr=countryUS";
                else if(value4.equals("United Kingdom"))
                    url+="&cr=countryGB";
                else if(value4.equals("Turkey"))
                    url+="&cr=countryTR";
                else if(value4.equals("Germany"))
                    url+="&cr=countryDE";
                else if(value4.equals("France"))
                    url+="&cr=countryFR";
                else if(value4.equals("Italy"))
                    url+="&cr=countryIT";
                else if(value4.equals("Spain"))
                    url+="&cr=countryES";
                else if(value4.equals("Russia"))
                    url+="&cr=countryRU";
                else if(value4.equals("Poland"))
                    url+="&cr=countryPL";
                else if(value4.equals("Slovenia"))
                    url+="&cr=countrySI";
                else if(value4.equals("Crotia"))
                    url+="&cr=countryHR";
            }


        }
        return url;
    }


    public void ghdb()
    {
        ghdb_label.setText("<html>These are the google dorks which are created before by other people.<br>These google dorks may help you to understand what" +
                " kind of google searches can find any kind of exploits.<br> You can see the dork (complete search text) and the url of it in the table.<br>" +
                "By clicking the button below the screen, you can use the dork you've been selected and see the results on web page.</html>");

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        List<String> link_list=new ArrayList<String>();
        List<String> dork_list=new ArrayList<String>();

        File input = new File("Ghdb/ghdb_1-3953.html");
        Document doc = null;

        try {
            doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements links = doc.select("a[href]");

        for (Element link : links) {
            link_list.add(link.attr("abs:href"));
            dork_list.add(link.text());

        }

        link_list.toArray();
        dork_list.toArray();

        Object[] columnNames = {"Google Dork", "Google Search Link"};
        DefaultTableModel model = new DefaultTableModel(new Object[0][0], columnNames){

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

        };

        for(int i=0;i<link_list.toArray().length;i++)
        {
            if(!dork_list.toArray()[i].equals("")) {
                Object[] o = new Object[2];
                o[0] = dork_list.toArray()[i];
                o[1] = link_list.toArray()[i];
                model.addRow(o);
            }
        }


        table1.setModel(model);


    }

    public void fill_combos()
    {
        String[] terms={"Anywhere in the page","In the url of the page","In the title of the page",
                "In the text of the page","In links to the page"};
        String[] filetypes={"Other","Adobe Flash (.swf)","Adobe Portable Document Format (.pdf)",
                "HTML (.html)","Microsoft Excel (.xlsx)", "Microsoft PowerPoint (.pptx)","Microsoft Word (.docx)",
                "OpenOffice presentation (.odp)","OpenOffice text (.odt)}", "Text (.txt)", "C source code (.c)",
                "C++ source code (.cpp)", "C# source code (.cs)","Java source code (.java)",
                "Python source code (.py)", "Wireless Markup Language (.wml)","XML (.xml)"};
        String[] updates={"Anytime","Last 1 Hour","Last 24 Hour","Last Week","Last Month","Last Year"};
        String[] languages={"Any Language","English","Turkish","German","French","Italian","Spanish","Russian","Polish","Slovenian","Crotian"};
        String[] regions={"Any Region","ABD","Turkey","United Kingdom","Germany","France","Italy","Spain","Russia","Poland","Slovenia","Crotia"};


        for(int i=0;i<terms.length;i++)
            terms_in.addItem(terms[i]);

        for(int i=0;i<filetypes.length;i++)
            file_types.addItem(filetypes[i]);

        for(int i=0;i<updates.length;i++)
            last_update.addItem(updates[i]);

        for(int i=0;i<languages.length;i++)
            lang_box.addItem(languages[i]);

        for(int i=0;i<regions.length;i++)
            regions_box.addItem(regions[i]);
    }

    public void exploit() {
        exploit_label.setText("<html>This is the list of up-to-date exploits from Exploit-DB.<br> All of the exploits are obtained from Internet " +
                "in every use of this application.<br> You can see the exploit description and the url for the details of the exploit.<br>" +
                "By clicking the button below the screen, you can reach the website to see more detailed view about selected exploit.</html>");
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Object[] columnNames = {"Exploit Description","Exploit Details Link"};
        DefaultTableModel model = new DefaultTableModel(new Object[0][0], columnNames){

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

        };

        ArrayList<String> exp_desc = new ArrayList<>();
        ArrayList<String> exp_link = new ArrayList<>();
        String url = "https://www.exploit-db.com/browse/";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        Element table = doc.select("table.exploit_list").first();
        Elements links1 = table.select("a[href]");
        Elements rows = table.select("tr");
        Elements els = doc.getElementsByClass("description");


        for (Element link : links1) {

            exp_link.add(link.attr("abs:href"));
        }
        for (Element link : els) {

            exp_desc.add(link.text());
        }

        for(int i=0;i<exp_desc.toArray().length;i++)
        {

                Object[] o = new Object[2];
                o[0] = exp_desc.toArray()[i];
                o[1] =exp_link.toArray()[i];

                model.addRow(o);

        }
        table2.setModel(model);
    }

}
