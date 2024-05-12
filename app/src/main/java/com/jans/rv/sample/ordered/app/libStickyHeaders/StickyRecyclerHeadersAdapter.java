package com.jans.rv.sample.ordered.app.libStickyHeaders;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface StickyRecyclerHeadersAdapter<VH extends RecyclerView.ViewHolder> {

  long getHeaderId(int position);

  VH onCreateHeaderViewHolder(ViewGroup parent);

  void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

  int getItemCount();

}
