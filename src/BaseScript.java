import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.Spell;
import org.dreambot.api.methods.magic.cost.Rune;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Timer;

import config.Config;
import org.dreambot.api.wrappers.items.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.*;
import java.awt.*;
import java.util.List;


@ScriptManifest(name = "lilac's tele-alcher", description = "tele-alchs so you don't have to", author = "lilac",
        version = 1.2, category = Category.MAGIC, image = "https://i.imgur.com/Or3O0fh.png")


public class BaseScript extends AbstractScript {

    Config config = Config.getConfig();

    Map<String, Spell> teleDestinations = new HashMap<>();
    Map<String, Spell> alchVersion = new HashMap<>();

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private long ScriptStartTime;



    @Override
    public void onStart(){

        SwingUtilities.invokeLater(
                this::createGUI
        );

        ScriptStartTime = System.currentTimeMillis();
    }

    @Override
    public int onLoop() {

//        if(config.currentState().equals("TELE_ALCH")){
//            teleAlch(config.itemToALch());
//        }

        switch(config.currentState()){

            case "START":
                setup();

            case "TELE_ALCH":
                teleAlch(config.itemToALch());

            case "ARR_BANK":
                bankOperations();

        }

        return 1;
    }

    @Override
    public void onPaint(Graphics g) {

        Color transparentBlack = new Color(0f, 0f, 0f, .3f);

        g.setColor(transparentBlack);
        g.fillRect(40, 105, 200,100);

        g.setColor(Color.white);
        g.drawString("lilac tele-alcher - V1.2", 45, 125);

        g.drawString("Script status: " + config.statusText(), 45, 150);

        g.drawString("Teleporting to: " + config.teleDestination(), 45, 170);

        g.drawString("Time running:" + Timer.formatTime(System.currentTimeMillis() - ScriptStartTime), 45, 190);


        g.drawString("state: " + config.currentState() , 45, 210);

    }


    private void teleAlch(String alchItem){

//        if(Magic.canCast((alchVersion.get(config.AlchSpell()))) && Inventory.contains(alchItem) && Magic.canCast(teleDestinations.get(config.teleDestination()))){
//
//            MouseSettings.setSpeed(getRandomNumber(4,8));
//
//            Magic.castSpell(alchVersion.get(config.AlchSpell()));
//
//            Inventory.interact(config.itemToALch());
//
//            Magic.castSpell(teleDestinations.get(config.teleDestination()));
//
//            Sleep.sleepWhile(() -> Players.getLocal().isAnimating(), 5000, 10);
//        } else {
//            endScript();
//        }

    }

    private void setup(){

        if(Bank.isOpen()){
            config.setState("ARR_BANK");
        }
        else{
            BankLocation nearestBank = BankLocation.getNearest();

            Walking.walk(nearestBank);

            config.setState("WALKING_TO_BANK");

            if(!Players.getLocal().isMoving() && !Objects.equals(config.currentState(), "TELE_ALCH")){
                config.setState("ARR_BANK");
            };
        }
    }

    private void bankOperations(){

        Bank.open();
        Bank.depositAllItems();

        Logger.log(config.itemToALch());

        Bank.withdrawAll(config.itemToALch());

        List<Item> equippedItems = Players.getLocal().getEquipment();

//        if(Players){
//
//        }

        config.setState("READY_TO_ALCH");


    }

    private void createGUI(){

        JFrame mainGUIframe = new JFrame();

        JFrame errorFrame = new JFrame();

        mainGUIframe.setTitle("lilac's tele-alcher");

        mainGUIframe.setResizable(false);

        mainGUIframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainGUIframe.setPreferredSize(new Dimension(300, 120));

        mainGUIframe.setLocationRelativeTo(Client.getCanvas());

        mainGUIframe.getContentPane().setLayout(new BorderLayout());

        JPanel settings = new JPanel();
        JPanel buttons = new JPanel();

        settings.setLayout(new GridLayout(0,2));
        buttons.setLayout(new GridLayout(1,0));

        JLabel itemName = new JLabel();
        itemName.setText("Item to alch:");

        itemName.setToolTipText("Case and space sensitive!");

        JTextField itemNameInput = new JTextField();

       JLabel teleName = new JLabel();
       teleName.setText("Teleport Destination:");

       teleDestinations.put("Varrock",Normal.VARROCK_TELEPORT);
       teleDestinations.put("Lumbridge",Normal.LUMBRIDGE_TELEPORT);
       teleDestinations.put("Falador",Normal.FALADOR_TELEPORT);
       teleDestinations.put("Teleport to POH",Normal.TELEPORT_TO_HOUSE);
       teleDestinations.put("Camelot",Normal.CAMELOT_TELEPORT);
       teleDestinations.put("Kourend Castle",Normal.TELEPORT_TO_KOUREND);
       teleDestinations.put("Ardougne",Normal.ARDOUGNE_TELEPORT);
       teleDestinations.put("Watchtower",Normal.WATCHTOWER_TELEPORT);
       teleDestinations.put("Trollheim",Normal.TROLLHEIM_TELEPORT);
       teleDestinations.put("Ape Atoll",Normal.APE_ATOLL_TELEPORT);

        JComboBox<String> teleComboBox = new JComboBox<>(teleDestinations.keySet().toArray(new String[0]));

        JLabel alchVer = new JLabel();
        alchVer.setText("Alch mode:");

        alchVersion.put("High Alch",Normal.HIGH_LEVEL_ALCHEMY);
        alchVersion.put("Low Alch",Normal.LOW_LEVEL_ALCHEMY);

        JComboBox<String> alchComboBox = new JComboBox<>(alchVersion.keySet().toArray(new String[0]));


        teleComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedKey = (String) teleComboBox.getSelectedItem();
                config.setTeleDestination(selectedKey);
            }
        });

        alchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedKey = (String) alchComboBox.getSelectedItem();
                config.setAlchSpell(selectedKey);
            }
        });

        JButton startButton = new JButton();
        startButton.setText("Start!");

        startButton.addActionListener(
                l -> {

                    config.setItemToAlch(itemNameInput.getText());
                    config.setState("START");
                    mainGUIframe.dispose();
//
//                    if(Magic.canCast(teleDestinations.get(config.teleDestination())) && Magic.canCast(alchVersion.get(config.AlchSpell()))){
//                        config.setStatusText("Tele-Alching");
//                        config.setItemToAlch(itemNameInput.getText());
//                        config.setState("Tele-Alch");
//                        mainGUIframe.dispose();
//                    }
//                    else{
//                        JOptionPane.showMessageDialog(errorFrame, "Please ensure you have the required runes to cast your selected spells!", "Lilac Tele-Alcher -- Error!", JOptionPane.ERROR_MESSAGE);
//                    }



                }
        );

        settings.add(itemName);
        settings.add(itemNameInput);
        settings.add(teleName);
        settings.add(teleComboBox);
        settings.add(alchVer);
        settings.add(alchComboBox);

        buttons.add(startButton);

        mainGUIframe.add(settings, BorderLayout.CENTER);
        mainGUIframe.add(buttons, BorderLayout.SOUTH);

        mainGUIframe.pack();
        mainGUIframe.setVisible(true);

    }

    private void endScript(){
        Tabs.logout();
        stop();
    }

}
