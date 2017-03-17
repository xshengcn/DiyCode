package com.xshengcn.diycode.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.xshengcn.diycode.R;
import com.xshengcn.diycode.model.project.Project;
import com.xshengcn.diycode.util.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

  private final ArrayList<Project> projects;
  private final Context context;

  @Inject public ProjectAdapter(Context context) {
    this.context = context;
    projects = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_project, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Project project = projects.get(position);
    Glide.with(context).load(project.projectCoverUrl).into(holder.avatar);
    holder.name.setText(project.category.name);
    holder.node.setText(project.subCategory.name);
    holder.date.setText(DateUtils.computePastTime(project.lastUpdatedAt));
    holder.title.setText(project.name);
    holder.description.setText(project.description);
  }

  @Override public int getItemCount() {
    return projects.size();
  }

  public void addProjects(List<Project> projects) {
    int positionStart = getItemCount();
    this.projects.addAll(projects);
  }

  public void clear() {
    projects.clear();
  }

  public ArrayList<Project> getProjects() {
    return projects;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.node) TextView node;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.item_header) RelativeLayout itemHeader;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
