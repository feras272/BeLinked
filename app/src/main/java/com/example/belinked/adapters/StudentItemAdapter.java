package com.example.belinked.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belinked.R;
import com.example.belinked.models.StudentItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StudentItemAdapter extends RecyclerView.Adapter<StudentItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<StudentItem> studentItemList;

    public StudentItemAdapter(Context context, ArrayList<StudentItem> studentItemList) {
        this.context = context;
        this.studentItemList = studentItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentItem student = studentItemList.get(position);
        holder.volunteerAddress.setText(student.getVolunteerAddress());
        holder.signedUp.setText(student.getSignedUp());
        holder.usernameStudent.setText(student.getStudentUsername());
        holder.emailStudent.setText(student.getStudentEmail());
        holder.imageViewStudent.setImageResource(R.drawable.twitter_logo);
    }

    @Override
    public int getItemCount() {
        return studentItemList.size();
    }

    public void filterList(ArrayList<StudentItem> filteredList) {
        studentItemList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView volunteerAddress;
        TextView signedUp;
        TextView usernameStudent;
        TextView emailStudent;
        ImageView imageViewStudent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            volunteerAddress = itemView.findViewById(R.id.tv_volunteer_address_student_item);
            signedUp = itemView.findViewById(R.id.tv_signed_student_item);
            usernameStudent = itemView.findViewById(R.id.tv_username_student_item);
            emailStudent = itemView.findViewById(R.id.tv_email_student_item);
            imageViewStudent = itemView.findViewById(R.id.iv_student_item);

        }
    }
}
