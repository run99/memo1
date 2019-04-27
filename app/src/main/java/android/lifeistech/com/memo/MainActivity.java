package android.lifeistech.com.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public Realm realm;

    public ListView listView;

    public CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        listView = (ListView) findViewById(R.id.listView);

        checkBox = (CheckBox)findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean check = checkBox.isChecked();
                Log.d("check", String.valueOf(check));


                final Memo memo = realm.where(Memo.class).findFirst();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm){
                        memo.isCheck = check;
                    }
                });
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo =(Memo) parent.getItemAtPosition(position);
                Intent intent =new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("updateDate", memo.updateDate);
                startActivity(intent);

            }
        });



    }

    void setMemo(){
        final Memo memo = realm.where(Memo.class).findFirst();

        if(memo != null){
            checkBox.setChecked(memo.isCheck);

        }
    }



    public void setMemoList(){
        RealmResults<Memo> results = realm.where(Memo.class).findAll();
        List<Memo> items = realm.copyFromRealm(results);

        MemoAdapter adapter = new MemoAdapter(this, R.layout.layout_item_memo, items);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setMemoList();
        setMemo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
    public void create(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

}
