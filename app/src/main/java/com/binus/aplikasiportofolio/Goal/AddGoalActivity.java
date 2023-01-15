package com.binus.aplikasiportofolio.Goal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.binus.aplikasiportofolio.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class AddGoalActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    EditText editName, editDesc, editPriority,editStartDate,editFinalDate;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference goalRef = db.collection("goal");

    final String TAG = "===AddGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        editName=findViewById(R.id.editName);
        editDesc=findViewById(R.id.editDesc);
        editPriority=findViewById(R.id.editPriority);

        editStartDate=findViewById(R.id.editStartDate);
        DatePickerDialog.OnDateSetListener startDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                editStartDate.setText(updateLabel());
            }
        };
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddGoalActivity.this,startDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editFinalDate=findViewById(R.id.editFinalDate);
        DatePickerDialog.OnDateSetListener finalDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                editFinalDate.setText(updateLabel());
            }
        };
        editFinalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddGoalActivity.this,finalDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
        return dateFormat.format(myCalendar.getTime());
    }

    public void submit(View view){
        Log.i(TAG,editStartDate.getText().toString());
        Goal goal = new Goal();
        goal.setName(editName.getText().toString());
        goal.setDescription(editDesc.getText().toString());
        goal.setPriority(1);
        if(!editPriority.getText().toString().isEmpty())
            goal.setPriority(Integer.parseInt(editPriority.getText().toString()));
        goal.setCompleted(false);
        Map<String, Object> data = goal.toFirestore();
        data.put("start_date",editStartDate.getText().toString()+ " 00:00:00");
        data.put("final_date",editFinalDate.getText().toString()+ " 00:00:00");

        if(!validateForm()){
            Toast.makeText(getApplicationContext(),
                    goal.getName() + "Please Fill all Form",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        goalRef.document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    }

    private boolean validateForm(){
        boolean isNameNull = editName.getText().toString().isEmpty();
        boolean isDscNull = editDesc.getText().toString().isEmpty();
        boolean isPriorityNull = editPriority.getText().toString().isEmpty();
        boolean isDateNull = editStartDate.getText().toString().isEmpty();
        boolean isFinalNull = editFinalDate.getText().toString().isEmpty();


        if (!(isNameNull || isDscNull || isDateNull || isPriorityNull || isFinalNull) ) {
            return true;
        } else {
            return false;
        }
    }
}