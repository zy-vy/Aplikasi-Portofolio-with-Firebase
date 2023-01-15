package com.binus.aplikasiportofolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.binus.aplikasiportofolio.Goal.AddGoalActivity;
import com.binus.aplikasiportofolio.Goal.Goal;
import com.binus.aplikasiportofolio.Goal.GoalExpandableAdapter;
import com.binus.aplikasiportofolio.Milestone.Milestone;
import com.binus.aplikasiportofolio.Repo.FirebaseRepo;
//import com.binus.aplikasiportofolio.Repo.GoalRepo;
//import com.binus.aplikasiportofolio.Util.DbHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static FirebaseRepo instance = null;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference goalRef = db.collection("goal");
    CollectionReference milestoneRef = db.collection("milestone");
    private final String TAG = "+++REPO";

    //    private DbHandler dbHandler = new DbHandler(this);
    private Goal goal = new Goal();
    private List<Goal> goalList = new ArrayList<>();
    private List<Milestone> milestoneList = new ArrayList<>();

    private ExpandableListView expandableListView;
    private GoalExpandableAdapter goalExpandableAdapter;
//    private GoalRepo goalRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        goal.setName("RPA");
//        goal.setDescription("implementasi RPA pada project");
//        goal.setPriority(5);
//        goal.setStartDate(Timestamp.valueOf("2023-02-01 00:00:00"));
//        goal.setFinalDate(Timestamp.valueOf("2023-05-01 00:00:00"));
//        goal.setCompleted(false);
//
//        Milestone milestone = new Milestone();
//
//        milestone.setName("riset");
//        milestone.setDescription("mencari data");
//        milestone.setDueDate(Timestamp.valueOf("2023-03-01 00:00:00"));
//        milestone.setCompleted(false);
//        milestone.setGoalId(1);

//        dbHandler.resetGoal();
//        dbHandler.resetMilestone();
//
//        dbHandler.insertGoal(goal);
//        dbHandler.insertMilestone(milestone);
//        dbHandler.insertMilestone(milestone);

//        goalRepo = new GoalRepo(dbHandler);
//        goalList= goalRepo.fetchGoalMilestone();

//        goalList = FirebaseRepo.getInstance().getAllGoal();
//        milestoneList=dbHandler.getAllMilestone();

//        for (Goal goal1 : goalList) {
//            Log.i("+++Main", goal1.getName());
////            goal1.setMilestoneList(milestoneList);
//        }
////
//        for (Milestone milestone1 : milestoneList) {
//            Log.i("+++Main", milestone1.getName());
//        }


        //        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        //        goalExpandableAdapter = new GoalExpandableAdapter(this, goalList );
        //        expandableListView.setAdapter(goalExpandableAdapter);

//        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    public void initView(){
        goalList = new ArrayList<>();
        goalRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                List<Goal> goalList = new ArrayList<>();
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (!snapshot.isEmpty()) {
                        Log.d(TAG, "DocumentSnapshot data: " + snapshot.size());
                        snapshot.getDocuments().forEach(documentSnapshot -> {
                            milestoneList = new ArrayList<>();
                            goalRef.document(documentSnapshot.getId()).collection("milestone").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> taskChild) {
                                    if (taskChild.isSuccessful()) {
                                        QuerySnapshot snapshotChild = taskChild.getResult();
                                        if (!snapshotChild.isEmpty()) {

                                            snapshotChild.getDocuments().forEach(documentSnapshotChild -> {
                                                        milestoneList.add(Milestone.fromDocument(documentSnapshotChild.getId(), documentSnapshotChild.getData()));
                                                    }
                                            );
                                            goalList.forEach(goal1 -> {
                                                if (goal1.getId().equals(milestoneList.get(0).getGoalRef())){
                                                    goal1.setMilestoneList(milestoneList);
                                                }
                                            });
                                        }

                                    }
                                }
                            });
                            goal = Goal.fromDocument(documentSnapshot.getId(), documentSnapshot.getData());

                            goalList.add(goal);
                        });
                        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
                        goalExpandableAdapter = new GoalExpandableAdapter(MainActivity.this, goalList);
                        goalExpandableAdapter.notifyDataSetChanged();
                        expandableListView.setAdapter(goalExpandableAdapter);

                        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
//                                onResume();
                                return false;
                            }
                        });

                        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                            @Override
                            public void onGroupExpand(int groupPosition) {
                                Toast.makeText(getApplicationContext(),
                                        goalList.get(groupPosition) + " List Expanded.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                            @Override
                            public void onGroupCollapse(int groupPosition) {
                                Toast.makeText(getApplicationContext(),
                                        goalList.get(groupPosition) + " List Collapsed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });

                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        goalList.get(groupPosition)
                                                + " -> "
                                                + goalList.get(groupPosition).getMilestoneList().get(
                                                childPosition), Toast.LENGTH_SHORT
                                ).show();
                                showEditTaskDialog(v,goalList.get(groupPosition).getMilestoneList().get(childPosition));
//                                onResume();

                                return false;
                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void addNewGoal(View view){
        Intent myIntent = new Intent(MainActivity.this, AddGoalActivity.class);
        myIntent.putExtra("key", "");

        MainActivity.this.startActivity(myIntent);


    }

    Button saveButton,cancelButton;
    EditText editName,editDesc,editDueDate;
    RadioButton rbSelected;
    RadioGroup rbIsCompleted;
    boolean isDone=false;
    final Calendar myCalendar= Calendar.getInstance();

    public void showEditTaskDialog(View view, Milestone milestone){
        final Dialog editDialog = new Dialog(view.getContext());
        editDialog.setTitle("Edit MileStone (task)");

        editDialog.setContentView(R.layout.activity_add_milestone);
        editDialog.setCanceledOnTouchOutside(false);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        editDialog.getWindow().setLayout((6 * width) / 6, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelButton = (Button) editDialog.findViewById(R.id.btn_cancel);
        saveButton = (Button) editDialog.findViewById(R.id.btn_submit);
        TextView tx =(TextView) editDialog.findViewById(R.id.textView);
        tx.setText("Edit Milestone(task");
        editName = editDialog.findViewById(R.id.txtTaskName);
        editDesc = editDialog.findViewById(R.id.txttaskDesc);
        rbSelected = editDialog.findViewById(R.id.rbFalse);
        rbIsCompleted = editDialog.findViewById(R.id.rbIsCompleted);
//        int selectedId = rbIsCompleted.getCheckedRadioButtonId();
//        rbSelected = editDialog.findViewById(selectedId);
        editDueDate=editDialog.findViewById(R.id.txtTaskDueDate);


        editName.setText(milestone.getName());
        editDesc.setText(milestone.getDescription());
        editDueDate.setText(milestone.getDueDate().toString().substring(0,10));
        RadioButton rb;
        if(milestone.getCompleted()){
            rb = editDialog.findViewById(R.id.rbTrue);
            rb.setChecked(true);
        }else{
            rb = editDialog.findViewById(R.id.rbFalse);
            rb.setChecked(true);
        }

        rbIsCompleted.setOnCheckedChangeListener((radioGroup, i) -> {
            rbSelected = (RadioButton) radioGroup.findViewById(i);
            if (rbSelected == radioGroup.findViewById(R.id.rbTrue))
                isDone = true;
        });

        DatePickerDialog.OnDateSetListener finalDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                editDueDate.setText(updateLabel());
            }
        };
        editDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(),finalDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    Toast.makeText(MainActivity.this, "Please fill all", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirestoreSave(goal,milestone);
                Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                editDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });

        editDialog.show();
    }

    private void FirestoreSave(Goal goal,Milestone milestone){

        milestone.setName(editName.getText().toString());
        milestone.setDescription(editDesc.getText().toString());
        milestone.setCompleted(isDone);
        milestone.setGoalRef(goal.getId());
        Map<String, Object> data = milestone.toFirestore();
        data.put("due_date",editDueDate.getText().toString()+ " 00:00:00");



        goalRef.document(goal.getId()).collection("milestone").document(milestone.getId()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getApplicationContext(),
                                goal.getName() + " success added ",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(getApplicationContext(),
                                goal.getName() + " FAILED ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        onResume();
    }

    private String updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
        return dateFormat.format(myCalendar.getTime());
    }

    private boolean validateForm(){
        boolean isNameNull = editName.getText().toString().isEmpty();
        boolean isDscNull = editDesc.getText().toString().isEmpty();
        boolean isDateNull = editDueDate.getText().toString().isEmpty();
        int isChecked = rbIsCompleted.getCheckedRadioButtonId();

        if (!(isNameNull || isDscNull || isDateNull ) && (isChecked > 0)) {
            saveButton.setBackgroundColor(Color.BLACK);
            saveButton.setEnabled(true);
            saveButton.setClickable(true);
            return true;
        } else {

            return false;
        }
    }



}