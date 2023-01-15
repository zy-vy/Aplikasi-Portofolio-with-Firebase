package com.binus.aplikasiportofolio.Milestone;

import com.binus.aplikasiportofolio.Goal.Goal;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Milestone {

    private String id;
    private String name;
    private String description;
    private Timestamp dueDate;
    private Boolean isCompleted;
    private Integer goalId;
    private String goalRef;


    public Milestone() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public String getGoalRef() {
        return goalRef;
    }

    public void setGoalRef(String goalRef) {
        this.goalRef = goalRef;
    }


//    public static List<Milestone> fromSnapshot (QuerySnapshot snapshot){
//        List<Goal> list = new ArrayList<>();
//        snapshot.getDocuments().forEach(documentSnapshot -> {
//            Goal goal = Goal.fromDocument(documentSnapshot.getId(), documentSnapshot.getData());
//            list.add(goal);
//        });
//        return list;
//    }

    public static Milestone fromDocument (String id, Map<String, Object> data){
        Milestone milestone = new Milestone();
        milestone.setId( id);
        milestone.setName((String) data.getOrDefault("name",""));
        milestone.setDescription((String) data.getOrDefault("description",""));
        milestone.setDueDate(stringToDate((String) data.getOrDefault("due_date","")));
            milestone.setCompleted ((Boolean) data.getOrDefault("is_completed",false));
        milestone.setGoalRef( (String) data.getOrDefault("goal_ref",""));
        return milestone;
    }

    @Override
    public String toString() {
        return "Milestone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", isCompleted=" + isCompleted +
                '}';
    }

    public Map<String,Object> toFirestore(){
        Map<String, Object> data = new HashMap<>();
        data.put("name",(name)!=null ? name:null);
        data.put("description",this.description!=null?description : null);
        data.put("due_date",dateToString(this.dueDate));
        data.put("is_completed",this.isCompleted);
        data.put("goal_ref",this.goalRef);
        return data;
    }

    private static String dateToString(Timestamp date){
        if(date==null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private static Timestamp stringToDate(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Timestamp date = Timestamp.valueOf(str);
            return date;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
