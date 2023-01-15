//package com.binus.aplikasiportofolio.Util;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import com.binus.aplikasiportofolio.Goal.Goal;
//import com.binus.aplikasiportofolio.Milestone.Milestone;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DbHandler extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "AppDatabase";
//    private static final int DATABASE_VERSION = 1;
//
//    private static final String TBL_GOAL = "goal";
//    private static final String TBL_GOAL_ID = "id";
//    private static final String TBL_GOAL_NAME = "name";
//    private static final String TBL_GOAL_DESCRIPTION = "description";
//    private static final String TBL_GOAL_PRIORITY = "priority";
//    private static final String TBL_GOAL_STARTDATE = "start_date";
//    private static final String TBL_GOAL_FINALDATE = "final_date";
//    private static final String TBL_GOAL_ISCOMPLETED = "is_completed";
//
//    private static final String TBL_MILESTONE = "milestone";
//    private static final String TBL_MILESTONE_ID = "id";
//    private static final String TBL_MILESTONE_NAME = "name";
//    private static final String TBL_MILESTONE_DESCRIPTION = "description";
//    private static final String TBL_MILESTONE_DUEDATE = "due_date";
//    private static final String TBL_MILESTONE_ISCOMPLETED = "is_completed";
//    private static final String TBL_MILESTONE_GOAL_ID = "goal_id";
//
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String query =
//                "CREATE TABLE " + TBL_GOAL + "("
//                        + TBL_GOAL_ID + " INTEGER PRIMARY KEY,"
//                        + TBL_GOAL_NAME + " TEXT,"
//                        + TBL_GOAL_DESCRIPTION + " TEXT,"
//                        + TBL_GOAL_PRIORITY + " INTEGER,"
//                        + TBL_GOAL_STARTDATE + " TEXT,"
//                        + TBL_GOAL_FINALDATE + " TEXT,"
//                        + TBL_GOAL_ISCOMPLETED + " INTEGER"
//
//                        + ")";
//        sqLiteDatabase.execSQL(query);
//
//        query =
//                "CREATE TABLE " + TBL_MILESTONE + "("
//                        + TBL_MILESTONE_ID + " INTEGER PRIMARY KEY,"
//                        + TBL_MILESTONE_NAME + " TEXT,"
//                        + TBL_MILESTONE_DESCRIPTION + " TEXT,"
//                        + TBL_MILESTONE_DUEDATE + " TEXT,"
//                        + TBL_MILESTONE_ISCOMPLETED + " INTEGER,"
//                        + TBL_MILESTONE_GOAL_ID + " INTEGER,"
//                        + "FOREIGN KEY (goal_id) "
//                        +    "REFERENCES goal (id) "
//                        + "ON UPDATE CASCADE " +
//                        "      ON DELETE CASCADE "
//                        + ")" ;
//        sqLiteDatabase.execSQL(query);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_GOAL);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_MILESTONE);
//
//        onCreate(sqLiteDatabase);
//        sqLiteDatabase.close();
//    }
//
//    public DbHandler(@Nullable Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    public void insertGoal(Goal goal){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(TBL_GOAL_NAME, goal.getName());
//        cv.put(TBL_GOAL_DESCRIPTION, goal.getDescription());
//        cv.put(TBL_GOAL_PRIORITY, goal.getPriority());
//        cv.put(TBL_GOAL_STARTDATE, dateToString(goal.getStartDate()));
//        cv.put(TBL_GOAL_FINALDATE, dateToString(goal.getFinalDate()));
//        cv.put(TBL_GOAL_ISCOMPLETED, goal.getCompleted()?1:0);
//
//        db.insert(TBL_GOAL, TBL_GOAL_ID, cv);
//        db.close();
//    }
//
//    public List<Goal> getAllGoal(){
//        List<Goal> goals = new ArrayList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TBL_GOAL;
//        Cursor cursor = db.rawQuery(query, null);
//
//        if( cursor.moveToFirst() ){
//            do{
//                Goal goal = new Goal();
//                goal.setId(Integer.parseInt(cursor.getString(0)));
//                goal.setName(cursor.getString(1));
//                goal.setDescription(cursor.getString(2));
//                goal.setPriority(Integer.parseInt(cursor.getString(3)));
//                goal.setStartDate(stringToDate(cursor.getString(4)));
//                goal.setFinalDate(stringToDate(cursor.getString(5)));
////                Log.d("+++DBGOAL",cursor.getString(6));
//                goal.setCompleted((Integer.parseInt(cursor.getString(6))) != 0);
//
//                goals.add(goal);
//            }while( cursor.moveToNext() );
//        }
//        return goals;
//    }
//
//    public Goal getGoalbyId(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TBL_GOAL + " WHERE " + TBL_GOAL_ID + " = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
//
//        if( cursor.moveToFirst() ){
//            Goal goal = new Goal();
//            goal.setId(Integer.parseInt(cursor.getString(0)));
//            goal.setName(cursor.getString(1));
//            goal.setDescription(cursor.getString(2));
//            goal.setPriority(Integer.parseInt(cursor.getString(3)));
//            goal.setStartDate(stringToDate(cursor.getString(4)));
//            goal.setFinalDate(stringToDate(cursor.getString(5)));
////            Log.d("+++DBGOAL",cursor.getString(6));
//            goal.setCompleted((Integer.parseInt(cursor.getString(6))) != 0);
//
//            return goal;
//        }
//        else{
//            return null;
//        }
//    }
//
//    public void updateGoal(Goal goal){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(TBL_GOAL_NAME, goal.getName());
//        cv.put(TBL_GOAL_DESCRIPTION, goal.getDescription());
//        cv.put(TBL_GOAL_PRIORITY, goal.getPriority());
//        cv.put(TBL_GOAL_STARTDATE, dateToString(goal.getStartDate()));
//        cv.put(TBL_GOAL_FINALDATE, dateToString(goal.getFinalDate()));
//        cv.put(TBL_GOAL_ISCOMPLETED, goal.getCompleted()?1:0);
//
//        db.update(TBL_GOAL, cv,
//                TBL_GOAL_ID + " = ?", new String[]{String.valueOf(goal.getId())});
//        db.close();
//    }
//
//    public void deleteGoal(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TBL_GOAL, TBL_GOAL_ID + " = ?", new String[]{String.valueOf(id)});
//        db.close();
//    }
//
//    public void resetGoal(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TBL_GOAL);
//    }
//
//    public void insertMilestone(Milestone milestone){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(TBL_MILESTONE_NAME, milestone.getName());
//        cv.put(TBL_MILESTONE_DESCRIPTION, milestone.getDescription());
//        cv.put(TBL_MILESTONE_DUEDATE, dateToString(milestone.getDueDate()));
//        cv.put(TBL_MILESTONE_ISCOMPLETED, milestone.getCompleted()?1:0);
//        cv.put(TBL_MILESTONE_GOAL_ID, milestone.getGoalId());
//
//        db.insert(TBL_MILESTONE, TBL_MILESTONE_ID, cv);
//        db.close();
//    }
//
//    public List<Milestone> getAllMilestone(){
//        List<Milestone> milestones = new ArrayList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TBL_MILESTONE;
//        Cursor cursor = db.rawQuery(query, null);
//
//        if( cursor.moveToFirst() ){
//            do{
//                Milestone milestone = new Milestone();
//                milestone.setId(Integer.parseInt(cursor.getString(0)));
//                milestone.setName(cursor.getString(1));
//                milestone.setDescription(cursor.getString(2));
//                milestone.setDueDate(stringToDate(cursor.getString(3)));
////                Log.d("+++DBGOAL",cursor.getString(3));
//                milestone.setCompleted((Integer.parseInt(cursor.getString(4))) != 0);
//                milestone.setGoalId((Integer.parseInt(cursor.getString(5))));
//
//                milestones.add(milestone);
//            }while( cursor.moveToNext() );
//        }
//        return milestones;
//    }
//
//    public List<Milestone> getMilestonesByGoal(Goal goal){
//        List<Milestone> milestones = new ArrayList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TBL_MILESTONE + " WHERE "+ TBL_MILESTONE_GOAL_ID + " = ?" ;
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(goal.getId())});
//
//        if( cursor.moveToFirst() ){
//            do{
//                Milestone milestone = new Milestone();
//                milestone.setId(Integer.parseInt(cursor.getString(0)));
//                milestone.setName(cursor.getString(1));
//                milestone.setDescription(cursor.getString(2));
//                milestone.setDueDate(stringToDate(cursor.getString(3)));
////                Log.d("+++DBGOAL",cursor.getString(3));
//                milestone.setCompleted((Integer.parseInt(cursor.getString(4))) != 0);
//                milestone.setGoalId((Integer.parseInt(cursor.getString(5))));
//
//                milestones.add(milestone);
//            }while( cursor.moveToNext() );
//        }
//        return milestones;
//    }
//
//    public Milestone getMilestonebyId(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TBL_MILESTONE + " WHERE " + TBL_MILESTONE_ID + " = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
//
//        if( cursor.moveToFirst() ){
//            Milestone milestone = new Milestone();
//            milestone.setId(Integer.parseInt(cursor.getString(0)));
//            milestone.setName(cursor.getString(1));
//            milestone.setDescription(cursor.getString(2));
//            milestone.setDueDate(stringToDate(cursor.getString(3)));
////            Log.d("+++DBGOAL",cursor.getString(3));
//            milestone.setCompleted((Integer.parseInt(cursor.getString(4))) != 0);
//            milestone.setGoalId((Integer.parseInt(cursor.getString(5))));
//
//            return milestone;
//        }
//        else{
//            return null;
//        }
//    }
//
//    public void updateMilestone(Milestone milestone){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(TBL_MILESTONE_NAME, milestone.getName());
//        cv.put(TBL_MILESTONE_DESCRIPTION, milestone.getDescription());
//        cv.put(TBL_MILESTONE_DUEDATE, dateToString(milestone.getDueDate()));
//        cv.put(TBL_MILESTONE_ISCOMPLETED, milestone.getCompleted()?1:0);
//        cv.put(TBL_MILESTONE_GOAL_ID, milestone.getGoalId());
//
//        db.update(TBL_MILESTONE, cv,
//                TBL_MILESTONE_ID + " = ?", new String[]{String.valueOf(milestone.getId())});
//        db.close();
//    }
//
//    public void deleteMilestone(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TBL_MILESTONE, TBL_MILESTONE_ID + " = ?", new String[]{String.valueOf(id)});
//        db.close();
//    }
//
//    public void resetMilestone(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from "+ TBL_MILESTONE);
//    }
//
//    private String dateToString(Timestamp date){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(date);
//    }
//
//    private Timestamp stringToDate(String str){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        try {
//            Timestamp date = Timestamp.valueOf(str);
//            return date;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
