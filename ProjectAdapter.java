package com.mobiledev.androidstudio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledev.androidstudio.R;
import com.mobiledev.androidstudio.models.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying Project items in a RecyclerView.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private final Context context;
    private final OnProjectClickListener listener;
    private List<Project> projects;
    private final SimpleDateFormat dateFormat;

    /**
     * Interface for handling project clicks
     */
    public interface OnProjectClickListener {
        void onProjectClick(Project project);
    }

    /**
     * Constructor
     *
     * @param context Context
     * @param listener Click listener
     */
    public ProjectAdapter(Context context, OnProjectClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.projects = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault());
    }

    /**
     * Set the projects list and notify the adapter
     *
     * @param projects List of projects
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        
        holder.nameTextView.setText(project.getName());
        holder.packageTextView.setText(project.getPackageName());
        
        Date lastOpened = project.getLastOpened();
        if (lastOpened != null) {
            holder.dateTextView.setText(dateFormat.format(lastOpened));
        } else {
            holder.dateTextView.setText(R.string.project_never_opened);
        }
        
        // Set favorite star visibility
        holder.favoriteImageView.setVisibility(project.isFavorite() ? 
                View.VISIBLE : View.INVISIBLE);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProjectClick(project);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    /**
     * ViewHolder for project items
     */
    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView packageTextView;
        TextView dateTextView;
        ImageView favoriteImageView;

        ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_project_name);
            packageTextView = itemView.findViewById(R.id.text_project_package);
            dateTextView = itemView.findViewById(R.id.text_project_date);
            favoriteImageView = itemView.findViewById(R.id.image_project_favorite);
        }
    }
}