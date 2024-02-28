package config;

public class Config {

    private static final Config config = new Config();

    private String state = "";

    private String statusText = "Loading...";


    private String itemToAlch;

    private String teleDestination = "Loading...";

    private String alchSpell;


    private Config(){}

    public static Config getConfig() {
        return config;
    }

    public String currentState(){
      return state;
   }

    public void setState(String state){
       this.state = state;
   }

   public String itemToALch(){
       return itemToAlch;
   }

   public void setItemToAlch(String item){
       this.itemToAlch = item;
   }

   public String statusText(){
       return statusText;
   }

   public void setStatusText(String status){
       this.statusText = status;
   }

   public String teleDestination(){
       return teleDestination;
   }

   public void setTeleDestination(String destination){
       this.teleDestination = destination;
   }

   public String AlchSpell(){
       return alchSpell;
   }

    public void setAlchSpell(String spell){
        this.alchSpell = spell;
    }

}
