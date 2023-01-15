package com.binus.aplikasiportofolio.Goal;

import android.util.Log;

import androidx.annotation.Nullable;

import com.binus.aplikasiportofolio.Milestone.Milestone;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Goal {

    private String id;
    private String name;
    private String description;
    private Integer priority;
    private Timestamp startDate;
    private Timestamp finalDate;
    private Boolean isCompleted;
    private List<Milestone> milestoneList;

    public Goal() {
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

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(@Nullable int priority) {
        this.priority = priority;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(@Nullable Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(@Nullable Timestamp finalDate) {
        this.finalDate = finalDate;
    }

    public List<Milestone> getMilestoneList() {
        if(this.milestoneList==null){
            return new ArrayList<>();
        }

        return milestoneList;
    }

    public void setMilestoneList(@Nullable List<Milestone> milestoneList) {
        this.milestoneList = milestoneList;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(@Nullable Boolean completed) {
        isCompleted = completed;
    }

    public static List<Goal> fromSnapshot (QuerySnapshot snapshot){
        List<Goal> list = new ArrayList<>();
        snapshot.getDocuments().forEach(documentSnapshot -> {
            Goal goal = Goal.fromDocument(documentSnapshot.getId(), documentSnapshot.getData());
            list.add(goal);
        });
        return list;
    }

    public static Goal fromDocument (String id,Map<String, Object> data){
        Goal goal = new Goal();
        goal.setId( id);
        goal.setName((String) data.getOrDefault("name",""));
        goal.setDescription((String) data.getOrDefault("description",""));
        goal.setPriority(((Long)data.getOrDefault("priority",0l)).intValue());
        goal.setStartDate(stringToDate((String) data.getOrDefault("start_date","")));
        goal.setFinalDate(stringToDate((String) data.getOrDefault("final_date","")));
        goal.setCompleted ((Boolean) data.getOrDefault("is_completed", false));
        return goal;
    }

    public Map<String,Object> toFirestore(){
        Map<String, Object> data = new HashMap<>();
        data.put("name",(name)!=null ? name:null);
        data.put("description",this.description!=null?description : null);
        data.put("priority",this.priority!=null ?priority : 0);
        data.put("start_date",dateToString(this.startDate));
        data.put("final_date",dateToString(this.finalDate));
        data.put("is_completed",this.isCompleted);
        return data;
    }

    public static String dateToString(@Nullable Timestamp date){
        if(date==null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static Timestamp stringToDate(@Nullable String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Timestamp date = Timestamp.valueOf(str);
            Log.i("+++", "stringToDate: "+date);
            return date;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", startDate=" + startDate +
                ", finalDate=" + finalDate +
                ", isCompleted=" + isCompleted +
                ", milestoneList=" + milestoneList +
                '}';
    }
}
