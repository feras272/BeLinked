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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrganizationHomeAdapter extends RecyclerView.Adapter<OrganizationHomeAdapter.MyViewHolder> {

    Context context;
    ArrayList<StudentItem> organizationHomeList;

    public OrganizationHomeAdapter(Context context, ArrayList<StudentItem> studentItemList) {
        this.context = context;
        this.organizationHomeList = studentItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.organization_home_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentItem student = organizationHomeList.get(position);
        holder.volunteerAddress.setText(student.getVolunteerAddress());
        //holder.signedUp.setText(student.getSignedUp());
        holder.usernameStudent.setText(student.getStudentUsername());
        holder.emailStudent.setText(student.getStudentEmail());
        holder.volunteerDescription.setText(student.getVolunteerDescription());
        try {
            // receive then set
            Picasso.get().load(student.signedUp).into(holder.imageViewStudent);

        } catch (Exception e) {
            //Picasso.get().load(R.drawable.be_linked_logo).into(holder.imageViewStudent);
            holder.imageViewStudent.setImageResource(R.drawable.be_linked_logo);
        }

    }

    @Override
    public int getItemCount() {
        return organizationHomeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView volunteerAddress;
        //TextView signedUp;
        TextView usernameStudent;
        TextView emailStudent;
        TextView volunteerDescription;
        ImageView imageViewStudent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            volunteerAddress = itemView.findViewById(R.id.tv_volunteer_address_organization_home_item);
            //signedUp = itemView.findViewById(R.id.tv_signed_student_item);
            usernameStudent = itemView.findViewById(R.id.tv_username_organization_home_item);
            emailStudent = itemView.findViewById(R.id.tv_email_organization_home_item);
            imageViewStudent = itemView.findViewById(R.id.iv_organization_home_item);
            volunteerDescription = itemView.findViewById(R.id.tv_volunteer_description_organization_home_item);

        }
    }
}
