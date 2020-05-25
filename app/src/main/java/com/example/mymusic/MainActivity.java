
package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.ViewDragHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;




import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView myListViewForSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
    private static final int REQUEST_PERMISSIONS = 12345;
    private static final int PERMISSIONS_COUNT = 1;
    @SuppressLint("NewApi")
    private boolean arePermissionsDenied(){
        for(int i=0; i<PERMISSIONS_COUNT; i++ ){
            if(checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode ,permissions,grantResults);
        if(arePermissionsDenied()){
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        }else{
            onResume();
        }
    }
    private boolean isMusizPlayerInit;
    private List<String> musicFilesList;
    private void addMusicFilesFrom(String dirPath){
        final File musicDir = new File(dirPath);
        if(!musicDir.exists()){
            musicDir.mkdir();
            return;
        }
        final File[] files =musicDir.listFiles();
        for(File file: files){
            final String path= file.getAbsolutePath();
            if(path.endsWith(".mp3")){
                musicFilesList.add(path);
            }
        }
    }
    private void fillMusicList() {
        musicFilesList.clear();
        addMusicFilesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC)));
        addMusicFilesFrom(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
    }
        private void playMusicFile(String path){
            MediaPlayer mp= new MediaPlayer();
            try{
                mp.setDataSource(path);
                mp.prepare();
                mp.start();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    @Override
    protected void onResume(){
        //devam et
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        if(! isMusizPlayerInit){
            final ListView listView= findViewById(R.id.listViewistView);
            final TextAdapter textAdapter = new TextAdapter();
            musicFilesList=  new ArrayList<>();
            fillMusicList();
            textAdapter.setData(musicFilesList);
            listView.setAdapter(textAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String musicFilePath=musicFilesList.get(position);
                }
            });
            isMusizPlayerInit= true;
        }
    }
class TextAdapter extends BaseAdapter{
    private List<String> data =new ArrayList<>();
    void setData(List<String> mData){
        data.clear();
        data.addAll(mData);
        notifyDataSetChanged();
    }
    @Override
    public int getCount(){
        return data.size();
    }
    @Override
    public String getItem(int position){
        return null;
    }
    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        if (converView == null) {
            converView= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item, parent, false);
            converView.setTag(new ViewHolder((TextView) converView.findViewById(R.id.myItem)));

        }
        ViewHolder holder= (ViewHolder) converView.getTag();
        final String item= data.get(position);
        holder.info.setText(item.substring( item.lastIndexOf('/')+1));
        return converView;

    }
    class ViewHolder{
        TextView info;
        ViewHolder(TextView mInfo){
            info= mInfo;

        }
    }

}}