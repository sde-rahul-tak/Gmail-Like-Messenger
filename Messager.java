import java.util.*;
import java.util.Date;

class Msg {
    public boolean star;
    public boolean sent;
    public boolean read;
    public String dt;
    public String to;
    public String from;
    public String text;
    public Msg link;

    public Msg() {
        star = false;
        sent = false;
        read = true;
        to = "";
        from = "";
        text = "";
        link = null;
    }
}

class User {
    public boolean logged_in;
    public String username;
    public String password;
    public Msg headS;
    public Msg headR;
    public List<Msg> trash = new ArrayList<>();
    public User next;
    public User prev;

    public User() {
        logged_in = false;
        username = "";
        password = "";
        headS = null;
        headR = null;
        next = null;
        prev = null;
    }

    private int input_num(String prompt, Scanner sc) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.println("\nInvalid input. Try again. ");
            System.out.print(prompt);
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume buffer
        return val;
    }

    public void display_msgs(String title, Msg head) {
        String[] R = {"unread", "read"};
        String[] S = {"unstarred", "starred"};
        System.out.print("\n******************************* " + title + " *******************************");

        if (head == null)
            System.out.print("\nNo messages to display yet!\n");
        else {
            int i = 1;
            System.out.print("\n-------------------------------------------------------------------------------------------------");
            System.out.print(String.format("\n%-5s %-15s %-15s %-15s %-14s %-10s %-14s", "No.", "From", "To", "Message", "When", "Status", "Starred"));
            System.out.print("\n-------------------------------------------------------------------------------------------------");

            Msg m = head;
            while (m != null) {
                String subText = m.text.length() > 8 ? m.text.substring(0, 8) : m.text;
                String datePart = m.dt.length() > 10 ? m.dt.substring(4, 10) : m.dt;

                System.out.print(String.format("\n%-5d %-15s %-15s %-15s %-14s %-10s %-14s", 
                    i, m.from, m.to, subText + "...", datePart, R[m.read ? 1 : 0], S[m.star ? 1 : 0]));
                System.out.print("\n-------------------------------------------------------------------------------------------------");
                m = m.link;
                i++;
            }
        }
    }

    public void msg_options(String title, Msg[] headRef, Scanner sc) {
        int ch;
        do {
            display_msgs(title, headRef[0]);
            if (headRef[0] == null) return;
            System.out.print("\n********* " + title + " OPTIONS **********");
            System.out.print("\n0. Exit");
            System.out.print("\n1. Read a message");
            System.out.print("\n2. Delete a message");
            System.out.print("\n3. Star/Unstar a message");
            ch = input_num("\nEnter your choice: ", sc);
            System.out.print("\n---------------------------------------------");

            switch (ch) {
                case 1 -> read_msg(headRef[0], sc);
                case 2 -> del_msg(headRef, sc);
                case 3 -> starUnstar_msg(headRef[0], sc);
            }
        } while (ch != 0);
    }

    public void read_msg(Msg head, Scanner sc) {
        int no = input_num("\nEnter message no. to read: ", sc);
        Msg ptr = head;
        for (int i = 1; i < no && ptr != null; i++) ptr = ptr.link;
        if (ptr == null || no < 1) { System.out.print("\nInvalid message no."); return; }

        System.out.print("\n..................................................................");
        System.out.print("\n************** MESSAGE " + no + " **************");
        System.out.print("\nFrom : " + ptr.from);
        System.out.print("\nTo : " + ptr.to);
        System.out.print("\nWhen : " + ptr.dt);
        System.out.print("\nMessage : \n" + ptr.text);
        System.out.print("\n...................................................................\n");
        ptr.read = true;
    }

    public void del_msg(Msg[] headRef, Scanner sc) {
        if (headRef[0] == null) { System.out.print("No messages found.\n"); return; }
        int no = input_num("\nEnter message no. to delete: ", sc);
        if (no < 1) { System.out.print("\nInvalid message no."); return; }

        Msg ptr = headRef[0], prev = headRef[0];
        if (no == 1) {
            headRef[0] = headRef[0].link;
            System.out.print("Message deleted successfully!!\n");
            trash.add(ptr);
            return;
        }
        for (int i = 1; i < no && ptr != null; i++) {
            prev = ptr;
            ptr = ptr.link;
        }
        if (ptr == null) { System.out.print("Invalid message no.\n"); return; }
        prev.link = ptr.link;
        trash.add(ptr);
        System.out.print("Message deleted successfully!!\n");
    }

    public void starUnstar_msg(Msg head, Scanner sc) {
        int no = input_num("\nEnter message no. to star/unstar: ", sc);
        Msg ptr = head;
        for (int i = 1; i < no && ptr != null; i++) ptr = ptr.link;
        if (ptr == null || no < 1) { System.out.print("\nInvalid message no."); return; }
        ptr.star = !ptr.star;
        System.out.print("Message no. " + no + (ptr.star ? " has been starred.\n" : " has been unstarred.\n"));
    }

    public void search_msg(String title, Msg[] headRef, Scanner sc) {
        System.out.print("\nEnter the username: ");
        String un = sc.next();
        if (headRef[0] == null) { System.out.print("\nNo messages to display yet!"); return; }

        int ch;
        String[] R = {"unread", "read"};
        String[] S = {"unstarred", "starred"};

        do {
            List<Msg> results = new ArrayList<>();
            int i = 0;
            boolean found = false;
            Msg m = headRef[0];

            while (m != null) {
                String cmp = title.equals("SENT TO ") ? m.to : m.from;
                if (cmp.equals(un)) {
                    if (!found) {
                        System.out.print("\n**************************** MESSAGES " + title + un + " ****************************");
                        System.out.print("\n-------------------------------------------------------------------------------------------------");
                        System.out.print(String.format("\n%-5s %-15s %-15s %-15s %-14s %-10s %-14s", "No.", "From", "To", "Message", "When", "Status", "Starred"));
                        System.out.print("\n-------------------------------------------------------------------------------------------------");
                    }
                    i++; found = true; results.add(m);
                    System.out.print(String.format("\n%-5d %-15s %-15s %-15s %-14s %-10s %-14s", 
                        i, m.from, m.to, m.text.substring(0, Math.min(m.text.length(), 8)) + "...", m.dt.substring(4, 10), R[m.read ? 1 : 0], S[m.star ? 1 : 0]));
                    System.out.print("\n-------------------------------------------------------------------------------------------------");
                }
                m = m.link;
            }
            if (!found) { System.out.print("\nNo messages found!\n"); return; }

            System.out.print("\n********* MESSAGE OPTIONS **********");
            System.out.print("\n0. Exit\n1. Read a message\n2. Delete a message\n3. Star/Unstar a message");
            ch = input_num("\nEnter your choice: ", sc);
            switch (ch) {
                case 1 -> vec_read_msg(results, sc);
                case 2 -> vec_del_msg(results, headRef, sc);
                case 3 -> vec_starUnstar(results, sc);
            }
        } while (ch != 0);
    }

    public void starred_msg(String title, Msg[] headRef, Scanner sc) {
        int ch;
        do {
            List<Msg> results = new ArrayList<>();
            int i = 0; boolean found = false;
            Msg m = headRef[0];
            while (m != null) {
                if (m.star) {
                    if (!found) {
                        System.out.print("\n**************************** STARRED MESSAGES IN " + title + " ****************************");
                        System.out.print("\n-------------------------------------------------------------------------------------------------");
                        System.out.print(String.format("\n%-5s %-15s %-15s %-15s %-14s %-10s %-14s", "No.", "From", "To", "Message", "When", "Status", "Starred"));
                        System.out.print("\n-------------------------------------------------------------------------------------------------");
                    }
                    i++; found = true; results.add(m);
                    System.out.print(String.format("\n%-5d %-15s %-15s %-15s %-14s %-10s %-14s", 
                        i, m.from, m.to, m.text.substring(0, Math.min(m.text.length(), 8)) + "...", m.dt.substring(4, 10), m.read ? "read" : "unread", "starred"));
                    System.out.print("\n-------------------------------------------------------------------------------------------------");
                }
                m = m.link;
            }
            if (!found) { System.out.print("\nNo messages found!\n"); return; }
            System.out.print("\n********* MESSAGE OPTIONS **********");
            System.out.print("\n0. Exit\n1. Read a message\n2. Delete a message\n3. Star/Unstar a message");
            ch = input_num("\nEnter your choice: ", sc);
            switch (ch) {
                case 1 -> vec_read_msg(results, sc);
                case 2 -> vec_del_msg(results, headRef, sc);
                case 3 -> vec_starUnstar(results, sc);
            }
        } while (ch != 0);
    }

    public void trash_options(Scanner sc) {
        int ch;
        do {
            if (trash.isEmpty()) { System.out.print("Trash empty\n"); return; }
            System.out.print("\n******************************* TRASH *******************************");
            System.out.print("\n-------------------------------------------------------------------------------------------------");
            System.out.print(String.format("\n%-5s %-15s %-15s %-15s %-14s %-10s %-14s", "No.", "From", "To", "Message", "When", "Status", "Starred"));
            System.out.print("\n-------------------------------------------------------------------------------------------------");
            for (int i = 0; i < trash.size(); i++) {
                Msg m = trash.get(i);
                System.out.print(String.format("\n%-5d %-15s %-15s %-15s %-14s %-10s %-14s", 
                    i + 1, m.from, m.to, m.text.substring(0, Math.min(m.text.length(), 8)) + "...", m.dt.substring(4, 10), m.read ? "read" : "unread", m.star ? "starred" : "unstarred"));
                System.out.print("\n-------------------------------------------------------------------------------------------------");
            }
            System.out.print("\n********* TRASH OPTIONS **********");
            System.out.print("\n0. Exit\n1. Delete a message permanently\n2. View a message");
            ch = input_num("\nEnter your choice: ", sc);
            if (ch == 1) del_permanently(sc);
            else if (ch == 2) read_trashMsg(sc);
        } while (ch != 0);
    }

    public void del_permanently(Scanner sc) {
        int no = input_num("\nEnter message no. to delete: ", sc);
        if (no > trash.size() || no < 1) { System.out.print("Invalid message no.\n"); return; }
        trash.remove(no - 1);
        System.out.print("Message permanently deleted\n");
    }

    public void read_trashMsg(Scanner sc) {
        int no = input_num("\nEnter message no. to read: ", sc);
        if (no < 1 || no > trash.size()) { System.out.print("\nInvalid message no."); return; }
        Msg m = trash.get(no - 1);
        System.out.print("\n************** MESSAGE " + no + " **************\nFrom: " + m.from + "\nTo: " + m.to + "\nMessage: " + m.text + "\n");
        m.read = true;
    }

    private void vec_read_msg(List<Msg> results, Scanner sc) {
        int no = input_num("\nEnter message no. to read: ", sc);
        if (no < 1 || no > results.size()) return;
        Msg ptr = results.get(no - 1);
        System.out.print("\nMessage : \n" + ptr.text + "\n");
        ptr.read = true;
    }

    private void vec_del_msg(List<Msg> results, Msg[] headRef, Scanner sc) {
        int no = input_num("\nEnter message no. to delete: ", sc);
        if (no < 1 || no > results.size()) return;
        Msg target = results.get(no - 1);
        if (headRef[0] == target) headRef[0] = headRef[0].link;
        else {
            Msg curr = headRef[0];
            while (curr != null && curr.link != target) curr = curr.link;
            if (curr != null) curr.link = target.link;
        }
        trash.add(target);
        results.remove(no - 1);
        System.out.print("Message deleted successfully!!\n");
    }

    private void vec_starUnstar(List<Msg> results, Scanner sc) {
        int no = input_num("\nEnter message no. to star/unstar: ", sc);
        if (no < 1 || no > results.size()) return;
        results.get(no - 1).star = !results.get(no - 1).star;
    }
}

public class Messager {
    private User start = null, last = null;
    private Scanner sc = new Scanner(System.in);

    private int input_num(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.println("\nInvalid input. Try again. ");
            System.out.print(prompt);
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    public void create() {
        User tmp = new User();
        System.out.print("\nEnter username to create : ");
        tmp.username = sc.next();
        User ptr = start;
        while (ptr != null) {
            if (ptr.username.equals(tmp.username)) {
                System.out.print("\nEntered username already exists.");
                return;
            }
            ptr = ptr.next;
        }
        System.out.print("\nCreate password: ");
        tmp.password = sc.next();
        if (start == null) start = last = tmp;
        else { last.next = tmp; tmp.prev = last; last = tmp; }
        System.out.print("\nYour account has been created successfully!");
    }

    public void login() {
        System.out.print("\nEnter username: ");
        String un = sc.next();
        for (User ptr = start; ptr != null; ptr = ptr.next) {
            if (ptr.username.equals(un)) {
                System.out.print("\nEnter password: ");
                if (ptr.password.equals(sc.next())) {
                    System.out.print("\nSuccessfully logged in.");
                    activity(ptr);
                    return;
                } else { System.out.print("\nIncorrect password. Try again."); return; }
            }
        }
        System.out.print("\nUsername not found.");
    }

    public void remove() {
        System.out.print("\nEnter username: ");
        String un = sc.next();
        for (User curr = start; curr != null; curr = curr.next) {
            if (curr.username.equals(un)) {
                System.out.print("\nEnter the password: ");
                if (curr.password.equals(sc.next())) {
                    System.out.print("Are you sure you want to delete your account?(Y/N): ");
                    if (sc.next().equalsIgnoreCase("Y")) {
                        if (curr == start) start = curr.next;
                        if (curr.next != null) curr.next.prev = curr.prev;
                        if (curr.prev != null) curr.prev.next = curr.next;
                        System.out.print("\nYour account has been deleted successfully!");
                    }
                    return;
                }
            }
        }
        System.out.print("\nUsername not found.");
    }

    public void change_pw() {
        System.out.print("\nEnter username: ");
        String un = sc.next();
        for (User ptr = start; ptr != null; ptr = ptr.next) {
            if (ptr.username.equals(un)) {
                System.out.print("\nEnter previous password: ");
                if (ptr.password.equals(sc.next())) {
                    System.out.print("\nEnter new password : ");
                    ptr.password = sc.next();
                    System.out.print("\nYour password has been changed successfully!");
                } else System.out.print("\nIncorrect previous password.\n");
                return;
            }
        }
    }

    public void activity(User ptr) {
        int ch;
        do {
            System.out.print("\n************* HELLO @" + ptr.username + " ! *************");
            System.out.print("\n0. Logout\n1. Check inbox messages\n2. Send a message\n3. View sent messages\n4. Search messages sent to an user\n5. Search messages received from an user\n6. View deleted messages\n7. View starred messages in Inbox\n8. View starred messages in Sentbox");
            ch = input_num("\nEnter your choice: ");
            System.out.print("\n------------------------------------------\n");
            Msg[] hR = {ptr.headR}; Msg[] hS = {ptr.headS};
            switch (ch) {
                case 0 -> { ptr.logged_in = false; System.out.print("\nSuccessfully logged out."); return; }
                case 1 -> { ptr.msg_options("INBOX", hR, sc); ptr.headR = hR[0]; }
                case 2 -> send_msg(ptr);
                case 3 -> { ptr.msg_options("SENT", hS, sc); ptr.headS = hS[0]; }
                case 4 -> { ptr.search_msg("SENT TO ", hS, sc); ptr.headS = hS[0]; }
                case 5 -> { ptr.search_msg("RECEIVED FROM ", hR, sc); ptr.headR = hR[0]; }
                case 6 -> ptr.trash_options(sc);
                case 7 -> { ptr.starred_msg("INBOX ", hR, sc); ptr.headR = hR[0]; }
                case 8 -> { ptr.starred_msg("SENTBOX ", hS, sc); ptr.headS = hS[0]; }
            }
        } while (ch != 0);
    }

    public void send_msg(User ptr) {
        Msg m = new Msg();
        System.out.print("Enter username of user to message : ");
        m.to = sc.next();
        for (User ptrT = start; ptrT != null; ptrT = ptrT.next) {
            if (ptrT.username.equals(m.to)) {
                System.out.print("\nEnter message you want to send to @" + m.to + " :\n");
                sc.nextLine(); m.text = sc.nextLine();
                m.read = false; m.dt = new Date().toString();
                m.from = ptr.username;
                m.link = ptrT.headR; ptrT.headR = m;

                Msg ms = new Msg();
                ms.sent = true; ms.to = m.to; ms.from = m.from; ms.dt = m.dt; ms.text = m.text;
                ms.link = ptr.headS; ptr.headS = ms;
                System.out.print("\nMessage sent successfully to @" + m.to);
                return;
            }
        }
        System.out.print("\nEntered username doesn't exist.\n");
    }

    public static void main(String[] args) {
        Messager A = new Messager();
        int ch;
        do {
            System.out.print("\n----------------------------------------");
            System.out.print("\n******** WELCOME TO MESSAGER **********");
            System.out.print("\n0. Exit application\n1. Create new account\n2. Login to your account\n3. Delete an existing account\n4. Change Password");
            ch = A.input_num("\nEnter your choice: ");
            System.out.print("\n----------------------------------------");
            switch (ch) {
                case 0 -> System.out.print("\n********* PROGRAM ENDED **********");
                case 1 -> A.create();
                case 2 -> A.login();
                case 3 -> A.remove();
                case 4 -> A.change_pw();
            }
        } while (ch != 0);
    }
}