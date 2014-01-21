package net.yunstudio.treeviewdemo;

import java.util.ArrayList;
import java.util.List;

import net.yunstudio.util.view.treeview.TreeItem;
import net.yunstudio.util.view.treeview.TreeView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private TreeView treeView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		treeView=(TreeView) findViewById(R.id.treeview);
		List<TreeItem> items=new ArrayList<TreeItem>();
        
        initData(items);
        treeView.initData(items, 0);
        treeView.enabledAnim(500);
        
    }
    //初始化一些测试数据
    public void initData(List<TreeItem> items) {
        for (int i=0;i<10;i++) {
//            TextView tv=new TextView(getActivity());
            Button button=new Button(this);
            button.setText("item"+i);
            final TreeItem item=new TreeItem(button, null);
            
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    treeView.bind(item);
                }
            });
            
            items.add(item);
            
            if(i%2==0){
                Button bt1=new Button(this);
                bt1.setText("item"+i);
                bt1.setClickable(true);
                final TreeItem item_1=new TreeItem(bt1, item);
                
                bt1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        treeView.bind(item_1);
                    }
                });
                items.add(item_1);
                
                if(i%4==0){
                    Button bt_2=new Button(this);
                    bt_2.setText("item"+i);
                    bt_2.setClickable(true);
                    final TreeItem item_2=new TreeItem( bt_2, item_1);
                    
                    bt_2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            treeView.bind(item_2);
                        }
                    });
                    items.add(item_2);
                }
                
            }
            
            
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
