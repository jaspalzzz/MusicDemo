package com.android.musicplayer;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.musicplayer.databinding.AudioListItemBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.Holder> {

    ArrayList<Audio> audioList = new ArrayList();
    AudioSelectListener listener;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AudioListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.audio_list_item, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.binding.setItem(audioList.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.audioSelected(position,audioList.get(position).getPath());
            }
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public void setData(ArrayList<Audio> audioList) {
        this.audioList = audioList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        AudioListItemBinding binding;

        public Holder(AudioListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setAudioSelectListener(AudioSelectListener listener) {
        if (listener == null) {
            return;
        }
        this.listener = listener;
    }

    interface AudioSelectListener {
        void audioSelected(int position, String path);
    }
}
