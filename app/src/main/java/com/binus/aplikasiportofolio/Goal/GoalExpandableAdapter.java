package com.binus.aplikasiportofolio.Goal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.binus.aplikasiportofolio.Goal.Goal;
import com.binus.aplikasiportofolio.MainActivity;
import com.binus.aplikasiportofolio.Milestone.AddMilestoneActivity;
import com.binus.aplikasiportofolio.Milestone.Milestone;
import com.binus.aplikasiportofolio.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalExpandableAdapter extends BaseExpandableListAdapter {

    final Calendar myCalendar= Calendar.getInstance();
    private EditText editName, editDesc,editDueDate;
    private RadioGroup rbIsCompleted;
    private RadioButton rbSelected;
    private Button saveButton , cancelButton;
    private Boolean isDone= false;

    EditText editTxtName, editTxtDesc, editTxtPriority,editTxtStartDate,editTxtFinalDate;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference goalRef = db.collection("goal");


    final String TAG = "===addTask";

    private Context context;
    private List<Goal> goalList;

    public GoalExpandableAdapter(Context context, List<Goal> listGoal  ) {
        this.context = context;
        this.goalList = listGoal;
    }

    @Override
    public int getGroupCount() {
        return this.goalList.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return goalList.get(listPosition).getMilestoneList().size();
    }

    @Override
    public Goal getGroup(int listPosition) {
        return this.goalList.get(listPosition);
    }

    @Override
    public Milestone getChild(int listPosition, int expandedListPosition) {
        return goalList.get(listPosition).getMilestoneList().get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Goal goal =  getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.txtName);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(goal.getName());

        TextView txtStartDate = (TextView) convertView
                .findViewById(R.id.txtStartDate);
        txtStartDate.setText(Goal.dateToString(goal.getStartDate()).substring(0,10));

        TextView txtFinalDate = (TextView) convertView
                .findViewById(R.id.txtFinalDate);
        txtFinalDate.setText(Goal.dateToString(goal.getFinalDate()).substring(0,10));

        Button btnAddMilestone = (Button) convertView
                .findViewById(R.id.btnAddMilestone);
        Button btnEditGoal = (Button) convertView
                .findViewById(R.id.btnEditGoal);

        btnAddMilestone.setOnClickListener(view -> {
            Toast.makeText(context.getApplicationContext(), goal.getName(),Toast.LENGTH_SHORT).show();
//            Intent myIntent = new Intent(context.getApplicationContext(), AddMilestoneActivity.class);
//            myIntent.putExtra("goal_id", "");
//            context.getApplicationContext().startActivity(myIntent);
            showAddMilestoneDialog(view,goal);
        });

        btnEditGoal.setOnClickListener(view -> {
            Toast.makeText(context.getApplicationContext(), goal.getName(),Toast.LENGTH_SHORT).show();
//            Intent myIntent = new Intent(view.getContext(),EditGoalActivity.class);
//            myIntent.putExtra("goal",)
            showEditGoalDialog(view,goal);
        });

        return convertView;

    }



    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        final Milestone milestone = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expanded_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        TextView txtDuedate = (TextView) convertView
                .findViewById(R.id.expandedListDueDate);
        txtDuedate.setText(Goal.dateToString(milestone.getDueDate()).substring(0,10));
        expandedListTextView.setText(milestone.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


    public void showAddMilestoneDialog(View view,Goal goal){
        final Dialog addDialog = new Dialog(view.getContext());
        addDialog.setTitle("Add MileStone (task)");

        addDialog.setContentView(R.layout.activity_add_milestone);
        addDialog.setCanceledOnTouchOutside(false);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        addDialog.getWindow().setLayout((6 * width) / 6, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelButton = (Button) addDialog.findViewById(R.id.btn_cancel);
        saveButton = (Button) addDialog.findViewById(R.id.btn_submit);

        editName = addDialog.findViewById(R.id.txtTaskName);
        editDesc = addDialog.findViewById(R.id.txttaskDesc);
        rbSelected = addDialog.findViewById(R.id.rbFalse);
        rbIsCompleted = addDialog.findViewById(R.id.rbIsCompleted);
//        int selectedId = rbIsCompleted.getCheckedRadioButtonId();
//        rbSelected = addDialog.findViewById(selectedId);
        rbIsCompleted.setOnCheckedChangeListener((radioGroup, i) -> {
            rbSelected = (RadioButton) radioGroup.findViewById(i);
            if (rbSelected == radioGroup.findViewById(R.id.rbTrue))
                isDone = true;
        });






        editDueDate=addDialog.findViewById(R.id.txtTaskDueDate);
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
                    Toast.makeText(context.getApplicationContext(), "Please fill all", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirestoreSave(goal);
                Toast.makeText(context.getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                addDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });

        addDialog.show();
    }

    private String updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
        return dateFormat.format(myCalendar.getTime());
    }

    private void FirestoreSave(Goal goal){
        Milestone milestone = new Milestone();
        milestone.setName(editName.getText().toString());
        milestone.setDescription(editDesc.getText().toString());
        milestone.setCompleted(isDone);
        milestone.setGoalRef(goal.getId());
        Map<String, Object> data = milestone.toFirestore();
        data.put("due_date",editDueDate.getText().toString()+ " 00:00:00");



        goalRef.document(goal.getId()).collection("milestone").document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                Toast.makeText(context.getApplicationContext(),
                        goal.getName() + " success added ",
                        Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
                Toast.makeText(context.getApplicationContext(),
                        goal.getName() + " FAILED ",
                        Toast.LENGTH_SHORT).show();
            }
        });
        context.startActivity(new Intent(context,MainActivity.class));

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

    private void showEditGoalDialog(View view, Goal goal) {
        final Dialog editDialog = new Dialog(view.getContext());
//        editDialog.setTitle("Edit Goal");

        editDialog.setContentView(R.layout.activity_edit_goal);
        editDialog.setCanceledOnTouchOutside(false);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        editDialog.getWindow().setLayout((6 * width) / 6, LinearLayout.LayoutParams.WRAP_CONTENT);
        saveButton = (Button) editDialog.findViewById(R.id.btnSubmitEdit);

        final String TAG = "===editGoal";

        editTxtName=editDialog.findViewById(R.id.editTxtName);
        editTxtDesc=editDialog.findViewById(R.id.editTxtDesc);
        editTxtPriority=editDialog.findViewById(R.id.editTxtPriority);

        editTxtStartDate=editDialog.findViewById(R.id.editTxtStartDate);
        editTxtFinalDate=editDialog.findViewById(R.id.editTxtFinalDate);

        editTxtName.setText(goal.getName());
        editTxtDesc.setText(goal.getDescription());
        editTxtPriority.setText(String.valueOf(goal.getPriority()));
        editTxtStartDate.setText(goal.getStartDate().toString().substring(0,10));
        editTxtFinalDate.setText(goal.getFinalDate().toString().substring(0,10));

        DatePickerDialog.OnDateSetListener startDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                editTxtStartDate.setText(updateLabel());
            }
        };
        editTxtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(),startDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener finalDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                editTxtFinalDate.setText(updateLabel());
            }
        };
        editTxtFinalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(),finalDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateEditForm()){
                    Toast.makeText(view.getContext(),"Please fill all",Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseUpdate(goal);
                Toast.makeText(view.getContext(),"save edit",Toast.LENGTH_SHORT).show();
            }
        });

        editDialog.show();
    }

    private void firebaseUpdate(Goal goal1){
        Goal goal = new Goal();
        goal.setName(editTxtName.getText().toString());
        goal.setDescription(editTxtDesc.getText().toString());
        goal.setPriority(1);
        if(!editTxtPriority.getText().toString().isEmpty())
            goal.setPriority(Integer.parseInt(editTxtPriority.getText().toString()));
        goal.setCompleted(false);
        Map<String, Object> data = goal.toFirestore();
        data.put("start_date",editTxtStartDate.getText().toString()+ " 00:00:00");
        data.put("final_date",editTxtFinalDate.getText().toString()+ " 00:00:00");

        goalRef.document(goal1.getId()).set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(context,
                                goal.getName() + " success added ",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(context,
                                goal.getName() + " FAILED ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        context.startActivity(new Intent(context,MainActivity.class));
    }

    private boolean validateEditForm(){
        boolean isNameNull = editTxtName.getText().toString().isEmpty();
        boolean isDscNull = editTxtDesc.getText().toString().isEmpty();
        boolean isPriorityNull = editTxtPriority.getText().toString().isEmpty();
        boolean isDateNull = editTxtStartDate.getText().toString().isEmpty();
        boolean isFinalNull = editTxtFinalDate.getText().toString().isEmpty();


        if (!(isNameNull || isDscNull || isDateNull || isPriorityNull || isFinalNull) ) {
            return true;
        } else {
            return false;
        }
    }
}
