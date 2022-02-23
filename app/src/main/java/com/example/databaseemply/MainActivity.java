package com.example.databaseemply;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    EditText Empno,EmpName,EmpAddress,EmpPhone,EmpSalary,Deptno,DeptName,DeptLoc,DeptEmp,delEmp;
    Button addEmp,addDept,Del,viewE,viewD;
    SQLiteDatabase db,db1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Empno=(EditText)findViewById(R.id.empno);
        EmpName=(EditText)findViewById(R.id.empname);
        EmpAddress=(EditText)findViewById(R.id.empaddr);
        EmpPhone=(EditText)findViewById(R.id.empphn);
        EmpSalary=(EditText)findViewById(R.id.empsal);
        Deptno=(EditText)findViewById(R.id.deptno);
        DeptName=(EditText)findViewById(R.id.deptname);
        DeptLoc=(EditText)findViewById(R.id.deptloc);
        DeptEmp=(EditText)findViewById(R.id.deptemp);
        delEmp=(EditText)findViewById(R.id.del);
        addEmp=(Button)findViewById(R.id.empBtn);
        addDept=(Button)findViewById(R.id.deptBtn);
        Del=(Button)findViewById(R.id.delBtn);
        viewE=(Button)findViewById(R.id.viewEmp);
        viewD=(Button)findViewById(R.id.viewDept);

        addEmp.setOnClickListener(this);
        addDept.setOnClickListener(this);
        Del.setOnClickListener(this);
        viewD.setOnClickListener(this);
        viewE.setOnClickListener(this);

        db=openOrCreateDatabase("EmployeeDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS employee(empno VARCHAR PRIMARY KEY,name VARCHAR,address VARCHAR,phone VARCHAR,salary FLOAT);");
        db1=openOrCreateDatabase("DepartmentDB", Context.MODE_PRIVATE,null);
        db1.execSQL("CREATE TABLE IF NOT EXISTS department(deptno VARCHAR PRIMARY KEY,dname VARCHAR,location VARCHAR,empNo VARCHAR REFERENCES employee(empno) ON DELETE CASCADE ON UPDATE CASCADE);");
    }

    @Override
    public void onClick(View v) {
        if (v == addEmp)
        {
            if (Empno.getText().toString().trim().length()==0 ||
                    EmpName.getText().toString().trim().length()==0 ||
                    EmpAddress.getText().toString().trim().length()==0||
                    EmpPhone.getText().toString().trim().length()==0||EmpSalary.getText().toString().trim().length()==0)
            {
                showMessage("Error","Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO employee VALUES('"+Empno.getText()+"','"+EmpName.getText()+"','"+EmpAddress.getText()+"','"+EmpPhone.getText()+"','"+EmpSalary.getText()+"');");
            showMessage("Success","Record Added");
            clearText();
        }
        if (v == addDept)
        {
            if (Deptno.getText().toString().trim().length()==0 ||
                    DeptName.getText().toString().trim().length()==0 ||
                    DeptLoc.getText().toString().trim().length()==0||
                    DeptEmp.getText().toString().trim().length()==0)
            {
                showMessage("Error","Please enter all values");
                return;
            }
            db1.execSQL("INSERT INTO department VALUES('"+Deptno.getText()+"','"+DeptName.getText()+"','"+DeptLoc.getText()+"','"+DeptEmp.getText()+"');");
            showMessage("Success","Record Added");
            clearText();
        }
        if (v == viewE)
        {
            Cursor c = db.rawQuery("SELECT * FROM employee;",null);
            if (c.getCount()==0)
            {
                showMessage("Error","No records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext())
            {
                buffer.append("Emp_no : "+c.getString(0)+"\n");
                buffer.append("Name : "+c.getString(1)+"\n");
                buffer.append("Address : "+c.getString(2)+"\n");
                buffer.append("Phone no. : "+c.getString(3)+"\n");
                buffer.append("Salary : "+c.getString(4)+"\n\n");
            }
            showMessage("Employee Details",buffer.toString());
        }
        if (v == viewD)
        {
            Cursor cursor = db1.rawQuery("SELECT * FROM department;",null);
            if (cursor.getCount()==0)
            {
                showMessage("Error","No records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext())
            {
                buffer.append("Dept_no : "+cursor.getString(0)+"\n");
                buffer.append("Name : "+cursor.getString(1)+"\n");
                buffer.append("Location : "+cursor.getString(2)+"\n");
                buffer.append("Emp_no : "+cursor.getString(3)+"\n\n");
            }
            showMessage("Department Details",buffer.toString());
        }
        if (v == Del)
        {
            if (delEmp.getText().toString().trim().length()==0)
            {
                showMessage("Error","Please enter Department name");
                return;
            }
            Cursor c =db1.rawQuery("SELECT * FROM department WHERE dname='"+delEmp.getText()+"'",null);
            if (c.moveToFirst())
            {
                db1.execSQL("DELETE FROM employee WHERE empno in ( select empno from department where dname='"+delEmp.getText()+"')");
                showMessage("Success","Record Deleted");
            }
            else
            {
                showMessage("Error","Invalid Department name");
            }
            clearText();
        }
    }

    private void clearText()
    {
        Empno.setText("");
        EmpName.setText("");
        EmpAddress.setText("");
        EmpPhone.setText("");
        EmpSalary.setText("");
        DeptName.setText("");
        Deptno.setText("");
        DeptLoc.setText("");
        DeptEmp.setText("");
        delEmp.setText("");
        Empno.requestFocus();
        DeptName.requestFocus();
    }
    private void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
