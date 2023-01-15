//package com.binus.aplikasiportofolio.Repo;
//
//import android.util.Log;
//
//import com.binus.aplikasiportofolio.Goal.Goal;
//import com.binus.aplikasiportofolio.Milestone.Milestone;
//import com.binus.aplikasiportofolio.Util.DbHandler;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class GoalRepo {
//    private DbHandler dbHandler;
//
//    public GoalRepo(DbHandler dbHandler) {
//        this.dbHandler = dbHandler;
//    }
//
//    public List<Goal> fetchGoalMilestone(){
//        return dbHandler.getAllGoal().stream().map(goal -> {
//            List<Milestone> milestoneList = dbHandler.getMilestonesByGoal(goal);
//            goal.setMilestoneList(milestoneList);
//            Log.i("+++Repo", "fetchGoalMilestone: "+ goal.toString());
//            Log.i("+++Repo", "fetchGoalMilestone: "+ milestoneList.get(0).getName());
//
//            return goal;
//        }).collect(Collectors.toList());
//    }
//
//}
