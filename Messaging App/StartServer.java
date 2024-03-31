import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Group project for project 5
 *
 * @author Parth Thakre, Anthony Rodriguez, Will Greenwood, Marcelo Moreno, Ji Bing Ni
 * @version 2023-04-12
 */

class MessagingServer extends Thread {
    private HashMap<String, ArrayList<String>> messages;
    private String currentUser;
    private Customer customer;
    private Seller seller;
    private ArrayList<String> oppositeUsers;
    private ArrayList<Customer> customers;
    private ArrayList<Seller> sellers;
    private ArrayList<Seller> blockedSellers;
    private ArrayList<Customer> blockedCustomers;
    private ArrayList<Seller> unmessagedSellers;
    private ArrayList<Customer> unmessagedCustomers;
    private Scanner scanner;
    private String totals;
    private String sentStats;
    private Socket socket;

    private String sellerReceivedStats;
    private String unsorted;

    static Object o;

    public MessagingServer(Socket socket) {
        currentUser = "";
        messages = new HashMap<String, ArrayList<String>>();
        oppositeUsers = new ArrayList<String>();
        customers = new ArrayList<Customer>();
        sellers = new ArrayList<Seller>();
        blockedSellers = new ArrayList<Seller>();
        blockedCustomers = new ArrayList<Customer>();
        unmessagedCustomers = new ArrayList<Customer>();
        unmessagedSellers = new ArrayList<Seller>();
        scanner = new Scanner(System.in);
        customer = null;
        seller = null;
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                String line = reader.readLine();
                String action = line.substring(0, line.indexOf(":"));
                if (action.equalsIgnoreCase("Login Customer")) {
                    String[] userInfo = line.substring(line.indexOf(":") + 1).split(",");
                    boolean successful = loginCustomer(userInfo[0], userInfo[1]);
                    if (successful == false) {
                        Customer x = null;
                    }
                    String[] messagesWith = getListOfMessagesWithSellers();
                    String[] newMessagesList = getListOfNewConversationsWithSellers();
                    oos.writeObject(customer);
                    oos.writeObject(messagesWith);
                    oos.writeObject(newMessagesList);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Login Seller")) {
                    String[] userInfo = line.substring(line.indexOf(":") + 1).split(",");
                    boolean successful = loginSeller(userInfo[0], userInfo[1]);
                    if (successful == false) {
                        Seller x = null;
                    }
                    String[] messagesWith = getListOfMessagesWithCustomers();
                    String[] newMessagesList = getListOfNewConversationsWithCustomers();
                    oos.writeObject(seller);
                    oos.writeObject(messagesWith);
                    oos.writeObject(newMessagesList);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Create Customer")) {
                    String[] userInfo = line.substring(line.indexOf(":") + 1).split(",");
                    boolean successful = makeNewCustomer(userInfo[0], userInfo[1], userInfo[2]);
                    if (successful == false) {
                        Customer x = null;
                    }
                    String[] messagesWith = getListOfMessagesWithSellers();
                    String[] newMessagesList = getListOfNewConversationsWithSellers();
                    oos.writeObject(customer);
                    oos.writeObject(messagesWith);
                    oos.writeObject(newMessagesList);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Create Seller")) {
                    String[] userInfo = line.substring(line.indexOf(":") + 1).split(",");
                    boolean successful = makeNewSeller(userInfo[0], userInfo[1], userInfo[2], userInfo[3]);
                    if (successful == false) {
                        Seller x = null;
                    }
                    String[] messagesWith = getListOfMessagesWithCustomers();
                    String[] newMessagesList = getListOfNewConversationsWithCustomers();
                    oos.writeObject(seller);
                    oos.writeObject(messagesWith);
                    oos.writeObject(newMessagesList);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Logout")) {
                    if (customer != null && currentUser != null)
                        customer.updateInfo();
                    else if (seller != null && currentUser != null)
                        seller.updateInfo();
                    break;
                }
                if (action.equalsIgnoreCase("Load Messages")) {
                    String messageUser = line.substring(line.indexOf(":") + 1);
                    String messageLog = getMessageLog(messageUser);
                    oos.writeObject(messageLog);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Send Message")) {
                    String message = formatMessage(line.substring(line.indexOf(":") + 1));
                    String info = reader.readLine();
                    String keyUser = info.split(",")[0];
                    String otherUser = info.split(",")[1];
                    String conversationFile;
                    if (keyUser.equalsIgnoreCase("Seller"))
                        conversationFile = customer.getUsername() + "&" + otherUser + ".txt";
                    else conversationFile = otherUser + "&" + seller.getUsername() + ".txt";
                    File f = new File(conversationFile);
                    ArrayList<String> lines = new ArrayList<String>();
                    try {
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        while (true) {
                            String x = bfr.readLine();
                            if (x == null)
                                break;
                            lines.add(x);
                        }
                        lines.add(message);
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    messages.replace(otherUser, lines);
                    writeConversation(keyUser, otherUser);
                    oos.writeObject(getMessageLog(otherUser));
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Start New")) {
                    String message = formatMessage(line.substring(line.indexOf(":") + 1));
                    String info = reader.readLine();
                    ArrayList<String> lines = new ArrayList<String>();
                    lines.add(message);
                    String keyUser = info.split(",")[0];
                    String otherUser = info.split(",")[1];
                    messages.put(otherUser, lines);
                    writeConversation(keyUser, otherUser);
                    String[] messagesWith = new String[0];
                    String[] newMessagesList = new String[0];
                    if (keyUser.equalsIgnoreCase("Seller")) {
                        if (messages.size() > 0) {
                            Set<String> keys = messages.keySet();
                            Iterator<String> i = keys.iterator();
                            String[] allConversations = new String[messages.size()];
                            for (int x = 0; x < messages.size(); x++)
                                allConversations[x] = i.next();
                            customer.setMessagedSellers(allConversations);
                        }
                        for (int x = 0; x < unmessagedSellers.size(); x++) {
                            if (unmessagedSellers.get(x).getUsername().equals(otherUser))
                                unmessagedSellers.remove(x);
                        }
                        customer.updateInfo();
                        messagesWith = getListOfMessagesWithSellers();
                        newMessagesList = getListOfNewConversationsWithSellers();
                    } else {
                        if (messages.size() > 0) {
                            Set<String> keys = messages.keySet();
                            Iterator<String> i = keys.iterator();
                            String[] allConversations = new String[messages.size()];
                            for (int x = 0; x < messages.size(); x++)
                                allConversations[x] = i.next();
                            seller.setMessagedCustomers(allConversations);
                        }
                        for (int x = 0; x < unmessagedCustomers.size(); x++) {
                            if (unmessagedCustomers.get(x).getUsername().equals(otherUser))
                                unmessagedCustomers.remove(x);
                        }
                        seller.updateInfo();
                        messagesWith = getListOfMessagesWithCustomers();
                        newMessagesList = getListOfNewConversationsWithCustomers();
                    }
                    oos.writeObject(getMessageLog(otherUser));
                    oos.writeObject(messagesWith);
                    oos.writeObject(newMessagesList);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Change E-mail Customer")) {
                    String newEmail = line.substring(line.indexOf(":") + 1);
                    customer.setEmail(newEmail);
                    customer.updateInfo();
                    oos.writeObject(customer);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Change E-mail Seller")) {
                    String newEmail = line.substring(line.indexOf(":") + 1);
                    seller.setEmail(newEmail);
                    seller.updateInfo();
                    oos.writeObject(seller);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Add Store")) {
                    String newStore = line.substring(line.indexOf(":") + 1);
                    String[] stores = seller.getStoreName();
                    String[] newStores = new String[stores.length + 1];
                    for (int x = 0; x < stores.length; x++)
                        newStores[x] = stores[x];
                    newStores[newStores.length - 1] = newStore;
                    seller.setStoreName(newStores);
                    seller.updateInfo();
                    oos.writeObject(seller);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Export to CSV")) {
                    csvExport(line.substring(line.indexOf(":") + 1));
                }
                if (action.equalsIgnoreCase("Sort Stats by Messages Received")) {
                    totals = (customerStatsReceivedMessages());
                    //System.out.println(totals);
                    oos.writeObject(totals);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Sort Stats by Messages Sent")) {
                    sentStats = customerStatsSentMessages();
                    oos.writeObject(sentStats);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("View Sorted Stats1")) { // As a seller
                    sellerReceivedStats = sellerStatsReceivedMessages(true);
                    oos.writeObject(sellerReceivedStats);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Stats of Messages Received")) {
                    unsorted = sellerStatsReceivedMessages(false);
                    oos.writeObject(unsorted);
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Edit Message")) {
                    String info = line.substring(line.indexOf(":") + 1);
                    String lineToEdit = info.split(",")[0];
                    String newLine = info.split(",")[1];
                    String sendMessageTo = reader.readLine();
                    String keyUser = reader.readLine();
                    boolean lineFound = false;
                    for (int x = 0; x < messages.get(sendMessageTo).size(); x++) {
                        if (messages.get(sendMessageTo).get(x).contains(lineToEdit)) {
                            if (!messages.get(sendMessageTo).get(x).contains(sendMessageTo)) {
                                lineFound = true;
                                messages.get(sendMessageTo).set(x,
                                        messages.get(sendMessageTo).get(x).replace(lineToEdit, newLine));
                            }
                        }
                    }
                    if (lineFound) {
                        writer.println("true");
                        writer.flush();
                        writeConversation(keyUser, sendMessageTo);
                        oos.writeObject(getMessageLog(sendMessageTo));
                        oos.flush();
                    } else {
                        writer.println("false");
                        writer.flush();
                    }
                }
                if (action.equalsIgnoreCase("Delete Message")) {
                    String lineToDelete = line.substring(line.indexOf(":") + 1);
                    String sendMessageTo = reader.readLine();
                    String keyUser = reader.readLine();
                    boolean lineFound = false;
                    for (int x = 0; x < messages.get(sendMessageTo).size(); x++) {
                        if (messages.get(sendMessageTo).get(x).contains(lineToDelete)) {
                            if (!messages.get(sendMessageTo).get(x).contains(sendMessageTo)) {
                                lineFound = true;
                                messages.get(sendMessageTo).remove(x);
                            }
                        }
                    }
                    if (lineFound) {
                        writer.println("true");
                        writer.flush();
                        writeConversation(keyUser, sendMessageTo);
                        oos.writeObject(getMessageLog(sendMessageTo));
                        oos.flush();
                    } else {
                        writer.println("false");
                        writer.flush();
                    }
                }
                if (action.equalsIgnoreCase("Refresh")) {
                    String info = line.substring(line.indexOf(":") + 1);
                    String otherUser = info.split(",")[0];
                    String keyUser = info.split(",")[1];
                    String conversationFile;
                    if (keyUser.equalsIgnoreCase("Seller"))
                        conversationFile = customer.getUsername() + "&" + otherUser + ".txt";
                    else conversationFile = otherUser + "&" + seller.getUsername() + ".txt";
                    File f = new File(conversationFile);
                    ArrayList<String> lines = new ArrayList<String>();
                    try {
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        while (true) {
                            String x = bfr.readLine();
                            if (x == null)
                                break;
                            lines.add(x);
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    messages.replace(otherUser, lines);
                    writeConversation(keyUser, otherUser);
                    oos.writeObject(getMessageLog(otherUser));
                    oos.flush();
                }
                if (action.equalsIgnoreCase("Block")) {
                    String info = line.substring(line.indexOf(":") + 1);
                    String toBlock = info.split(",")[0];
                    String keyUser = info.split(",")[1];
                    if (keyUser.equalsIgnoreCase("Seller")) {

                        boolean doneBlocking = false;
                        for (int i = 0; i < sellers.size(); i++) {
                            if (sellers.get(i).getUsername().equals(toBlock)) {
                                messages.remove(toBlock);
                                blockedSellers.add(sellers.get(i));
                                sellers.remove(i);
                                doneBlocking = true;
                                unmessagedSellers.remove(toBlock);
                            }
                        }
                        if (doneBlocking) {
                            writer.println("Done Blocking");
                            writer.flush();
                        } else {
                            for (int i = 0; i < blockedSellers.size(); i++) {
                                if (blockedSellers.get(i).getUsername().equals(toBlock)) {
                                    sellers.add(blockedSellers.get(i));
                                    blockedSellers.remove(i);
                                    doneBlocking = true;
                                }
                            }
                            if (doneBlocking) {
                                writer.println("Done Unblocking");
                                writer.flush();
                            } else {
                                writer.println("Failed");
                                writer.flush();
                            }
                        }
                        if (doneBlocking) {
                            if (messages.size() > 0) {
                                Set<String> keys = messages.keySet();
                                Iterator<String> i = keys.iterator();
                                String[] messagesWith = new String[messages.size()];
                                for (int x = 0; x < messages.size(); x++)
                                    messagesWith[x] = i.next();
                                customer.setMessagedSellers(messagesWith);
                            }
                            String[] blockedUsers = new String[blockedSellers.size()];
                            for (int i = 0; i < blockedSellers.size(); i++) {
                                blockedUsers[i] = blockedSellers.get(i).getUsername();
                            }
                            customer.setBlockedSellers(blockedUsers);
                            customer.updateInfo();
                            String[] messagesWith = getListOfMessagesWithSellers();
                            String[] newMessagesList = getListOfNewConversationsWithSellers();
                            oos.writeObject(messagesWith);
                            oos.writeObject(newMessagesList);
                            oos.flush();
                        }
                    } else {

                        boolean doneBlocking = false;
                        for (int i = 0; i < customers.size(); i++) {
                            if (customers.get(i).getUsername().equals(toBlock)) {
                                messages.remove(toBlock);
                                blockedCustomers.add(customers.get(i));
                                customers.remove(i);
                                doneBlocking = true;
                                unmessagedCustomers.remove(toBlock);
                            }
                        }
                        if (doneBlocking) {
                            writer.println("Done Blocking");
                            writer.flush();
                        } else {
                            for (int i = 0; i < blockedCustomers.size(); i++) {
                                if (blockedCustomers.get(i).getUsername().equals(toBlock)) {
                                    customers.add(blockedCustomers.get(i));
                                    blockedCustomers.remove(i);
                                    doneBlocking = true;
                                }
                            }
                            if (doneBlocking) {
                                writer.println("Done Unblocking");
                                writer.flush();
                            } else {
                                writer.println("Failed");
                                writer.flush();
                            }
                        }
                        if (doneBlocking) {
                            if (messages.size() > 0) {
                                Set<String> keys = messages.keySet();
                                Iterator<String> i = keys.iterator();
                                String[] messagesWith = new String[messages.size()];
                                for (int x = 0; x < messages.size(); x++)
                                    messagesWith[x] = i.next();
                                seller.setMessagedCustomers(messagesWith);
                            }
                            String[] blockedUsers = new String[blockedCustomers.size()];
                            for (int i = 0; i < blockedCustomers.size(); i++) {
                                blockedUsers[i] = blockedCustomers.get(i).getUsername();
                            }
                            seller.setBlockedCustomers(blockedUsers);
                            seller.updateInfo();
                            String[] messagesWith = getListOfMessagesWithCustomers();
                            String[] newMessagesList = getListOfNewConversationsWithCustomers();
                            oos.writeObject(messagesWith);
                            oos.writeObject(newMessagesList);
                            oos.flush();
                        }
                    }
                }
            }
            oos.close();
            ois.close();
            writer.close();
            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String frequentlyUsedWord(String filename) {
        ArrayList<String> mostFrequentWords = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            HashMap<String, Integer> frequencyMap = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length > 1) {
                    String[] words = parts[1].trim().split("\\s+");
                    for (String word : words) {
                        if (frequencyMap.containsKey(word)) {
                            frequencyMap.put(word, frequencyMap.get(word) + 1);
                        } else {
                            frequencyMap.put(word, 1);
                        }
                    }
                }
            }

            int maxFrequency = 0;
            for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() > maxFrequency) {
                    maxFrequency = entry.getValue();
                    mostFrequentWords.clear();
                    mostFrequentWords.add(entry.getKey());
                } else if (entry.getValue() == maxFrequency) {
                    mostFrequentWords.add(entry.getKey());
                }
            }

            // Return the ArrayList of the most frequently used words
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return mostFrequentWords.toString();

    }

    public String sellerStatsReceivedMessages(boolean sort) {
        String total = null;
        total = "Statistics Sorted by Number of Messages Received from Customers\n" +
                "***************************************************************************\n";
        ArrayList<String> frequentWordByConvoList = new ArrayList<>();
        ArrayList<String> messagesReceivedEach = new ArrayList<>();
        String[] messagedCustomerList = (seller.getMessagedCustomers());
        ArrayList<String> customerList = new ArrayList<>();
        for (int i = 0; i < messagedCustomerList.length; i++) {
            File f = new File(messagedCustomerList[i] + "&" + seller.getUsername() + ".txt");
            String t = frequentlyUsedWord(messagedCustomerList[i] + "&" + seller.getUsername() + ".txt");
            frequentWordByConvoList.add(t);
            try {
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                bfr.readLine();
                int received = 0;
                String line = bfr.readLine();
                while (line != null) {
                    String sender = line.substring(line.indexOf(",") + 1, line.indexOf(": "));
                    if (!sender.equals(seller.getUsername())) {
                        received++;
                    }
                    line = bfr.readLine();
                }
                messagesReceivedEach.add(String.valueOf(received) + "," + messagedCustomerList[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (sort) {
            for (int i = 0; i < messagesReceivedEach.size(); i++) {
                messagesReceivedEach.set(i, messagesReceivedEach.get(i) + "%$%" + frequentWordByConvoList.get(i));
            }
            Collections.sort(messagesReceivedEach, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    int n1 = Integer.parseInt(s1.substring(0, s1.indexOf(",")));
                    int n2 = Integer.parseInt(s2.substring(0, s2.indexOf(",")));
                    return n2 - n1;
                }
            });
            for (int j = 0; j < messagedCustomerList.length; j++) {
                total += (j + 1 + ") Customer: " + messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf(",") + 1, messagesReceivedEach.get(j).indexOf("%$%"))
                        + "\n" + "Messages Received: " + messagesReceivedEach.get(j).substring(0, messagesReceivedEach.get(j).indexOf(","))) + "\n"
                        + "Most Commonly Used Words: " + messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf("%$%") + 3) +
                        "\n***************************************************************************\n";
            }


            return total;
        } else {
            for (int j = 0; j < messagedCustomerList.length; j++) {
                total += (j + 1 + ") Customer: " +
                        messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf(",") + 1)
                        + "\n" + "Messages Received: " +
                        messagesReceivedEach.get(j).substring(0, messagesReceivedEach.get(j).indexOf(","))) + "\n"
                        + "Most Commonly Used Words: " + frequentWordByConvoList.get(j) +
                        "\n***************************************************************************\n";
            }
            return total;
        }
    }

    public String customerStatsSentMessages() {
        String total = null;
        total = "Statistics Sorted by Number of Messages sent to Sellers\n" +
                "***************************************************************************\n";
        ArrayList<String> messagesReceivedEach = new ArrayList<>();
        String[] messagedSellersList = (customer.getMessagedSellers());
        ArrayList<String> storeList = new ArrayList<>();
        for (int i = 0; i < messagedSellersList.length; i++) {
            File fi = new File("UserInfo.txt");
            File f = new File(customer.getUsername() + "&" + messagedSellersList[i] + ".txt");
            try {
                FileReader fir = new FileReader(fi);
                BufferedReader bfrUserInfo = new BufferedReader(fir);
                String line2 = bfrUserInfo.readLine();
                int count = 1;
                while (line2 != null) {
                    if ((count + 2) % 3 == 0) {
                        String[] info = line2.split(",", 5);
                        if (info[3].equals("Seller")) {
                            if (info[0].equals(messagedSellersList[i])) {
                                storeList.add("[" + info[4] + "]");
                            }
                        }
                    }
                    count++;
                    line2 = bfrUserInfo.readLine();
                }
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                bfr.readLine();
                int sent = 0;

                String line = bfr.readLine();
                while (line != null) {
                    String sender = line.substring(line.indexOf(",") + 1, line.indexOf(": "));
                    if (sender.equals(customer.getUsername())) {
                        sent++;
                    }
                    line = bfr.readLine();
                }
                messagesReceivedEach.add(String.valueOf(sent) + "," + messagedSellersList[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < messagesReceivedEach.size(); i++) {
            messagesReceivedEach.set(i, messagesReceivedEach.get(i) + "%$%" + storeList.get(i));
        }
        Collections.sort(messagesReceivedEach, new Comparator<String>() {
            public int compare(String s1, String s2) {
                int n1 = Integer.parseInt(s1.substring(0, s1.indexOf(",")));
                int n2 = Integer.parseInt(s2.substring(0, s2.indexOf(",")));
                return Integer.compare(n1, n2);
            }
        });
        for (int j = 0; j < messagedSellersList.length; j++) {
            total += (j + 1 + ") Seller: " +
                    messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf(",") + 1,
                            messagesReceivedEach.get(j).indexOf("%$%"))
                    + ", Store Names: " +
                    messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf("%$%") + 3) + "\n"
                    + "Messages Sent: " +
                    messagesReceivedEach.get(j).substring(0, messagesReceivedEach.get(j).indexOf(","))) + "\n" +
                    "***************************************************************************\n";
        }
        return total;
    }

    public String customerStatsReceivedMessages() {
        String total = null;
        total = "Statistics Sorted by Number of Messages Received from Sellers\n" +
                "***************************************************************************\n";
        ArrayList<String> messagesReceivedEach = new ArrayList<>();
        String[] messagedSellersList = (customer.getMessagedSellers());
        ArrayList<String> storeList = new ArrayList<>();
        for (int i = 0; i < messagedSellersList.length; i++) {
            File fi = new File("UserInfo.txt");
            File f = new File(customer.getUsername() + "&" + messagedSellersList[i] + ".txt");
            try {
                FileReader fir = new FileReader(fi);
                BufferedReader bfrUserInfo = new BufferedReader(fir);
                String line2 = bfrUserInfo.readLine();
                int count = 1;
                while (line2 != null) {
                    if ((count + 2) % 3 == 0) {
                        String[] info = line2.split(",", 5);
                        if (info[3].equals("Seller")) {
                            if (info[0].equals(messagedSellersList[i])) {
                                storeList.add("[" + info[4] + "]");
                            }
                        }
                    }
                    count++;
                    line2 = bfrUserInfo.readLine();
                }
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                bfr.readLine();
                int sent = 0;
                int received = 0;
                String line = bfr.readLine();
                while (line != null) {
                    String sender = line.substring(line.indexOf(",") + 1, line.indexOf(": "));
                    if (!sender.equals(customer.getUsername())) {
                        received++;
                    }
                    line = bfr.readLine();
                }
                messagesReceivedEach.add(String.valueOf(received) + "," + messagedSellersList[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < messagesReceivedEach.size(); i++) {
            messagesReceivedEach.set(i, messagesReceivedEach.get(i) + "%$%" + storeList.get(i));
        }
        Collections.sort(messagesReceivedEach, new Comparator<String>() {
            public int compare(String s1, String s2) {
                int n1 = Integer.parseInt(s1.substring(0, s1.indexOf(",")));
                int n2 = Integer.parseInt(s2.substring(0, s2.indexOf(",")));
                return Integer.compare(n1, n2);
            }
        });
        for (int j = 0; j < messagedSellersList.length; j++) {
            total += (j + 1 + ") Seller: " +
                    messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf(",") + 1,
                            messagesReceivedEach.get(j).indexOf("%$%"))
                    + ", Store Names: " +
                    messagesReceivedEach.get(j).substring(messagesReceivedEach.get(j).indexOf("%$%") + 3) + "\n"
                    + "Messages Received: " +
                    messagesReceivedEach.get(j).substring(0, messagesReceivedEach.get(j).indexOf(","))) + "\n" +
                    "***************************************************************************\n";
        }
        return total;
    }

    /*
        Makes new Customer
     */
    public boolean makeNewCustomer(String u, String p, String em) {
        currentUser = u;
        String password = p;
        String email = em;

        File f = new File("UserInfo.txt");
        if (f.exists())
            readUsers("Seller");
        else {

            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            readUsers("Seller");
        }
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equals(currentUser)) {
                currentUser = null;
                return false;
            }
        }
        customer = new Customer(currentUser, password, email);
        return true;
    }

    public boolean makeNewSeller(String u, String p, String em, String st) {
        currentUser = u;
        String password = p;
        String email = em;
        String[] storeName = {st};
        File f = new File("UserInfo.txt");
        if (f.exists())
            readUsers("Customer");
        else {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            readUsers("Customer");
        }
        for (int i = 0; i < sellers.size(); i++) {
            if (sellers.get(i).getUsername().equals(currentUser)) {
                currentUser = null;
                return false;
            }
        }
        seller = new Seller(currentUser, password, email, storeName);
        return true;
    }

    public boolean loginCustomer(String u, String p) {
        boolean loggedIn = false;
        String username = u;
        String password = p;
        currentUser = username;
        readUsers("Seller");
        for (int i = 0; i < customers.size(); i++) {
            String user = customers.get(i).getUsername();
            String pass = customers.get(i).getPassword();
            String mail = customers.get(i).getEmail();
            if (username.equals(user) && pass.equals(password)) {
                loggedIn = true;
                customer = new Customer(currentUser, password, mail);
                if (messages.size() > 0) {
                    Set<String> keys = messages.keySet();
                    Iterator<String> y = keys.iterator();
                    String[] messagedUsers = new String[messages.size()];
                    int x = 0;
                    while (y.hasNext()) {
                        messagedUsers[x] = y.next();
                        x++;
                    }
                    customer.setMessagedSellers(messagedUsers);
                }
                break;
            }
        }
        return loggedIn;
    }

    public boolean loginSeller(String u, String p) {
        boolean loggedIn = false;
        String username = u;
        String password = p;
        currentUser = username;
        readUsers("Customer");
        for (int i = 0; i < sellers.size(); i++) {
            String user = sellers.get(i).getUsername();
            String pass = sellers.get(i).getPassword();
            String mail = sellers.get(i).getEmail();
            String[] store = sellers.get(i).getStoreName();
            if (username.equals(user) && pass.equals(password)) {
                loggedIn = true;
                seller = new Seller(currentUser, password, mail, store);
                if (messages.size() > 0) {
                    Set<String> keys = messages.keySet();
                    Iterator<String> y = keys.iterator();
                    String[] messagedUsers = new String[messages.size()];
                    int x = 0;
                    while (y.hasNext()) {
                        messagedUsers[x] = y.next();
                        x++;
                    }
                    seller.setMessagedCustomers(messagedUsers);
                }
                break;
            }
        }
        return loggedIn;
    }

    /*
        Reads all the existing users from the info file in order to check that they exist + calls method to
         import conversations
     */
    public synchronized void readUsers(String keyUser) {
        File f = new File("UserInfo.txt");
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            while (true) {

                String line = bfr.readLine(); // reads first line to check if the person is customer or seller
                if (line == null)
                    break;
                String messageList = bfr.readLine();
                String blockedList = bfr.readLine();
                String[] userMessages;
                String[] blockedUsers;
                if (messageList.equalsIgnoreCase("null"))
                    userMessages = null;
                else userMessages = messageList.split(",");

                if (blockedList.equalsIgnoreCase("null"))
                    blockedUsers = null;
                else blockedUsers = blockedList.split(",");

                String[] userInfo = line.split(","); // splits the line to read type of user

                if (userInfo[3].equals("Seller") && !blockedList.contains(currentUser)) {
                    //Reads all stores present in userInfo file
                    ArrayList<String> stores = new ArrayList<>();
                    for (int i = 0; i < userInfo.length - 4; i++) {
                        stores.add(userInfo[i + 4]);
                    }
                    sellers.add(new Seller(userInfo[0], userInfo[1], userInfo[2], stores.toArray(new String[0]), userMessages,
                            blockedUsers));
                    if (!messageList.contains(currentUser) && userInfo[3].equalsIgnoreCase(keyUser))
                        unmessagedSellers.add(new Seller(userInfo[0],
                                userInfo[1], userInfo[2], stores.toArray(new String[0]), userMessages, blockedUsers));
                } else if (userInfo[3].equals("Customer") && !blockedList.contains(currentUser)) {
                    customers.add(new Customer(userInfo[0], userInfo[1], userInfo[2], userMessages, blockedUsers));
                    if (!messageList.contains(currentUser) && userInfo[3].equalsIgnoreCase(keyUser))
                        unmessagedCustomers.add(new Customer(userInfo[0],
                                userInfo[1], userInfo[2], userMessages, blockedUsers));
                } else if (userInfo[3].equals("Seller") && blockedList.contains(currentUser)) {
                    //Reads all stores present in userInfo file
                    ArrayList<String> stores = new ArrayList<>();
                    for (int i = 0; i < userInfo.length - 4; i++) {
                        stores.add(userInfo[i + 4]);
                    }
                    blockedSellers.add(new Seller(userInfo[0], userInfo[1], userInfo[2],
                            stores.toArray(new String[0]), userMessages,
                            blockedUsers));
                } else if (userInfo[3].equals("Customer") && blockedList.contains(currentUser))
                    blockedCustomers.add(new Customer(userInfo[0], userInfo[1], userInfo[2], userMessages,
                            blockedUsers));

                if (userInfo[3].equalsIgnoreCase(keyUser)) {
                    if (messageList.contains(currentUser) && !blockedList.contains(currentUser)) {
                        readCoversation(keyUser, userInfo[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Imports the existing conversation with the other user and stores it as a hashmap for printing later/saving
        and modifying
     */
    public synchronized void readCoversation(String keyUser, String otherUser) {
        ArrayList<String> conversation = new ArrayList<String>();
        String conversationFile = "";
        if (keyUser.equalsIgnoreCase("Seller"))
            conversationFile = currentUser + "&" + otherUser + ".txt";
        else
            conversationFile = otherUser + "&" + currentUser + ".txt";
        File f = new File(conversationFile);
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine();
                if (line == null)
                    break;
                conversation.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        messages.put(otherUser, conversation);
    }

    public String[] findSellerStore(String name) {
        for (int i = 0; i < sellers.size(); i++)
            if (sellers.get(i).getUsername().equals(name))
                return sellers.get(i).getStoreName();
        return null;
    }

    public String[] getListOfMessagesWithSellers() {
        String[] listOfMessages;
        if (messages.size() != 0) {
            listOfMessages = new String[messages.size()];
            String[] userMessages = customer.getMessagedSellers();
            for (int i = 0; i < messages.size(); i++) {
                listOfMessages[i] = userMessages[i] + ", Stores: " + Arrays.toString(findSellerStore(userMessages[i]));
            }
        } else {
            listOfMessages = new String[1];
            listOfMessages[0] = "No messages with sellers";
        }
        return listOfMessages;
    }

    public String[] getListOfMessagesWithCustomers() {
        String[] listOfMessages;
        if (messages.size() != 0) {
            listOfMessages = new String[messages.size()];
            String[] userMessages = seller.getMessagedCustomers();
            for (int i = 0; i < messages.size(); i++) {
                listOfMessages[i] = userMessages[i];
            }
        } else {
            listOfMessages = new String[1];
            listOfMessages[0] = "No messages with customers";
        }
        return listOfMessages;
    }

    public String[] getListOfNewConversationsWithCustomers() {
        String[] listOfMessages;
        if (unmessagedCustomers.size() != 0) {
            listOfMessages = new String[unmessagedCustomers.size()];
            for (int i = 0; i < unmessagedCustomers.size(); i++) {
                listOfMessages[i] = unmessagedCustomers.get(i).getUsername();
            }
        } else {
            listOfMessages = new String[1];
            listOfMessages[0] = "No new Customers to be messaged";
        }
        return listOfMessages;
    }

    public String[] getListOfNewConversationsWithSellers() {
        String[] listOfMessages;
        if (unmessagedSellers.size() != 0) {
            listOfMessages = new String[unmessagedSellers.size()];
            for (int i = 0; i < unmessagedSellers.size(); i++) {
                listOfMessages[i] = unmessagedSellers.get(i).getUsername() + ", Store: " +
                        Arrays.toString(unmessagedSellers.get(i).getStoreName());
            }
        } else {
            listOfMessages = new String[1];
            listOfMessages[0] = "No new Sellers to be messaged";
        }
        return listOfMessages;
    }

    public synchronized void writeConversation(String keyUser, String otherUser) {
        String fileName;
        if (keyUser.equals("Seller"))
            fileName = currentUser + "&" + otherUser + ".txt";
        else
            fileName = otherUser + "&" + currentUser + ".txt";
        File f = new File(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            PrintWriter pw = new PrintWriter(fos);
            if (!messages.get(otherUser).get(0).contains("&Seller"))
                pw.println("Participants (Customer&Seller) : " + fileName);
            for (int i = 0; i < messages.get(otherUser).size(); i++)
                pw.println(messages.get(otherUser).get(i));
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // export to CVS method, should work for both user and seller
    public synchronized void csvExport(String conversationTextFile) {
        try {
            String participants = conversationTextFile.substring(0, conversationTextFile.length() - 4);


            BufferedReader bfr = new BufferedReader(new FileReader(conversationTextFile));
            File filecsv = new File(participants + ".csv");
            BufferedWriter bfw = new BufferedWriter(new FileWriter(filecsv));

            //Reading twice here because we have to skip first line of the conversation
            String line = bfr.readLine();
            line = bfr.readLine();
            bfw.write("Customer&Seller,Timestamp,Sender,Contents\n");
            while (line != null) {
                //Constants used to build CSV line, array used specifically to get timestamp
                String[] lineArr = line.split(" ");
                String content = line.substring(line.indexOf(":", 20) + 2);
                String sender = lineArr[3].substring(lineArr[3].indexOf(",") + 1, lineArr[3].indexOf(":"));

                //Final string that is going to be written to CSV file
                String temp = String.format("%s,%s,%s,\"%s\"\n", participants, lineArr[2], sender, content);
                bfw.write(temp);

                line = bfr.readLine();
            }

            //If everything worked succesfully close reader and writer objects and print to terminal.
            bfw.close();
            bfr.close();
        } catch (IOException e) {
            System.out.println("Error reading file!");
            e.printStackTrace();
        }
    }

    public String getMessageLog(String messageUser) {
        String messageLog = "";
        for (int i = 0; i < messages.get(messageUser).size(); i++)
            messageLog = messageLog + messages.get(messageUser).get(i) + "\n";
        return messageLog;
    }

    public String formatMessage(String line) {
        DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
        ZonedDateTime currentISTime = ZonedDateTime.now();
        ZonedDateTime currentETime =
                currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        String timeStamp = globalFormat.format(currentETime);
        return timeStamp + "," + currentUser + ": " + line;
    }
}

public class StartServer {
    private ArrayList<MessagingServer> connections = new ArrayList<MessagingServer>();

    public static void main(String[] args) {
        try {
            new StartServer().runProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runProgram() throws UnknownHostException, IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(4242); //port 4242 works
        while (true) {
            Socket socket = serverSocket.accept();
            MessagingServer ms = new MessagingServer(socket);
            connections.add(ms);
            ms.start();
        }
    }
}
