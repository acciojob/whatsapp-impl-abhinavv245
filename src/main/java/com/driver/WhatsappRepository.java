package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String name, String mobile) throws Exception{

        if (userMobile.contains(mobile)) throw new Exception("User already exists");
        User user= new User(name,mobile);
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group= new Group();
        if(users.size()==2){
            group.setName(users.get(1).getName());
            group.setNumberOfParticipants(2);
            return group;
        }
        customGroupCount++;
        group.setName("Group "+customGroupCount);
        group.setNumberOfParticipants(users.size());
        adminMap.put(group,users.get(0));
        groupUserMap.put(group,users);
        return group;

    }

    public int createMessage(String content) {
        messageId++;
        Message message= new Message(messageId,content,new Date());
        return message.getId();
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        List<Message> listOfMessages= new ArrayList<>();
        if(!groupUserMap.containsKey(group))  throw new Exception("Group does not exist");
        else if(!userExistsInGroup(group,sender))  throw new Exception("You are not allowed to send message");

        listOfMessages.add(message);
        groupMessageMap.put(group,listOfMessages);
        return listOfMessages.size();
    }
 public boolean userExistsInGroup(Group group,User sender){
        List<User> list= groupUserMap.get(group);
        if(list.contains(sender)) return true;
        return false;
 }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }else if(adminMap.get(group)!=approver) {
            throw new Exception("Approver does not have rights");
        }
        else if(!groupUserMap.get(group).contains(user)){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }



    public int removeUser(User user) {
        return 0;
    }

    public String findMessage(Date start, Date end, int k) {
        return null;
    }
}
