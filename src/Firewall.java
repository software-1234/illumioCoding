import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Firewall {
    String csv;

    //needed to have multiple values to a key so used a Hashmap with an arraylist as the value
    HashMap<String, ArrayList<String>> newRules = new HashMap<String, ArrayList<String>>();
    public Firewall(String csv){
        this.csv = csv;
        newRules = setParameters(csv);

    }
    public boolean acceptPacket(String direction, String protocol, int port, String ipAddress ){

        String packet = direction + "," + protocol + "," + port + ","  + ipAddress;

        if(newRules.containsKey(direction + "," + protocol)){
            ArrayList<String> ruleList = newRules.get(direction+ "," + protocol);
            if(ruleList.contains(packet)){
                return true;
            }
            else{
                //split the rule into its 4 parts
              for(String rule: ruleList){
                 String[] splitUp = rule.split(",");
                //if there are ranges for both port and ip
                 if(splitUp[2].contains("-") && splitUp[3].contains("-")) {
                     //more string parsing to get the exact ranges
                     String[] myPort = splitUp[2].split("-");
                     String[] myIp = splitUp[3].split("-");
                     int lowNum = Integer.parseInt(myPort[0]);
                     int highNum = Integer.parseInt(myPort[1]);
                     long lowIp = ipToLong(myIp[0]) ;
                     long highIp = ipToLong(myIp[1]);
                     //if ip and port both in between the low and high then true
                     //ipTolong converts ip to a long value
                    if(port>= lowNum && port<= highNum && ipToLong(ipAddress) >= lowIp && ipToLong(ipAddress) <= highIp  ){
                        return true;
                    }

                 }
                 //only ip
                 else if(splitUp[3].contains("-")){
                     String[] myIp = splitUp[3].split("-");
                     long lowIp = ipToLong(myIp[0]) ;
                     long highIp = ipToLong(myIp[1]);
                     //if ip is in between the low and high then true
                     if(ipToLong(ipAddress) >= lowIp && ipToLong(ipAddress) <= highIp  ){
                         return true;
                     }

                  }
                  //only range
                  else if(splitUp[2].contains("-")){
                     String[] myPort = splitUp[2].split("-");
                     int lowNum = Integer.parseInt(myPort[0]);
                     int highNum = Integer.parseInt(myPort[1]);
                     //if port is in between the low and high then true
                     if(port>= lowNum && port<= highNum ){
                         return true;
                     }

                 }


              }
            }


        }
        return false;
    }

    private HashMap<String, ArrayList<String>> setParameters(String csv){
        HashMap<String, ArrayList<String>> validRules = new HashMap<String, ArrayList<String>>();
        BufferedReader br = null;
        String line;
        String[] lineSplit;
        try {

            br = new BufferedReader(new FileReader(csv));
            ArrayList<String> myLine = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // use comma as separator

                lineSplit = line.split(",");
                // If the hashmap does not contains the first two parameters then add the whole line to the arraylist
                //else get the previous arraylist and add on to the array list
                    if(!validRules.containsKey(lineSplit[0] + ","+ lineSplit[1])) {
                        ArrayList<String> lineList = new ArrayList<String>();
                        lineList.add(line);
                        validRules.put(lineSplit[0] + "," + lineSplit[1], lineList);
                    }
                    else{
                        ArrayList<String> tempList  = validRules.get(lineSplit[0] + "," + lineSplit[1]);
                        tempList.add(line);
                        validRules.put(lineSplit[0] + "," + lineSplit[1], tempList);
                    }


            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return validRules;

    }

/*

The following method to help convert an IP to a long is from:
http://technojeeves.com/

 */

    public long ipToLong(String ipAddress) {
        long result = 0;
        String[] parsedIP = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {
            result |= (Long.parseLong(parsedIP[3 - i]) << (i * 8));
        }

        return result & 0xFFFFFFFF;
    }

    public static void main(String[] args){
        //change path to current user
        Firewall firewall = new Firewall("/path/to/test.csv");
        System.out.println(firewall.acceptPacket(null,null,0,null));
        System.out.println(firewall.acceptPacket("inbound", "tcp", 81, "192.168.1.2"));
        System.out.println(firewall.acceptPacket("inbound", "tcp", 80, "192.168.1.2"));
        System.out.println(firewall.acceptPacket("outbound", "udp", 1000, "255.255.255.255"));
        System.out.println(firewall.acceptPacket("outbound", "udp", 1000, "0.255.255.4"));
        System.out.println(firewall.acceptPacket("outbound", "udp", 999, "10.10.10.11"));
        System.out.println(firewall.acceptPacket("inbound", "tcp", 80, "192.168.1.2"));
        System.out.println(firewall.acceptPacket("aaa", "tcp", 80, "192.168.1.2"));
        System.out.println(firewall.acceptPacket("inbound", "tcp", -220, "192.168.1.2"));
        System.out.println(firewall.acceptPacket("", "", Integer.MAX_VALUE, ""));
    }

}
