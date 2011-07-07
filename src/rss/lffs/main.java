package rss.lffs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class main extends ListActivity {
	  private ArrayList<RSSItem> itemlist = null;
      private RSSListAdaptor rssadaptor = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         setContentView(R.layout.main);
         
         itemlist = new ArrayList<RSSItem>();
         
         new RetrieveRSSFeeds().execute();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            
            RSSItem data = itemlist.get(position);
            
            
            
            setContentView(R.layout.rssitemview2);
            
            //startActivity(intent);
    }

    private void retrieveRSSFeed(String urlToRssFeed,ArrayList<RSSItem> list)
{
    try
    {
       URL url = new URL(urlToRssFeed);
       SAXParserFactory factory = SAXParserFactory.newInstance();
       SAXParser parser = factory.newSAXParser();
       XMLReader xmlreader = parser.getXMLReader();
       RSSParser theRssHandler = new RSSParser(list);

       xmlreader.setContentHandler(theRssHandler);

       InputSource is = new InputSource(url.openStream());

       xmlreader.parse(is);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}

private class RetrieveRSSFeeds extends AsyncTask<Void, Void, Void>
{
    private ProgressDialog progress = null;
    
            @Override
            protected Void doInBackground(Void... params) {
                    retrieveRSSFeed("http://www.luisfonsi-fansite.es/rss.xml",itemlist);
                    
                    rssadaptor = new RSSListAdaptor(main.this, R.layout.rssitemview, R.layout.rssitemview2, itemlist);
                    
                    return null;
            }
    
            @Override
            protected void onCancelled() {
                    super.onCancelled();
            }
            
            @Override
            protected void onPreExecute() {
                    progress = ProgressDialog.show(
                                    main.this, null, "Cargando nuevas noticias...");
                    
                    super.onPreExecute();
            }
            
            @Override
            protected void onPostExecute(Void result) {
                    setListAdapter(rssadaptor);
                    
                    progress.dismiss();
                    
                    super.onPostExecute(result);
            }
            
            @Override
            protected void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);
            }
}

private class RSSListAdaptor extends ArrayAdapter<RSSItem>{
    private List<RSSItem> objects = null;
    
            public RSSListAdaptor(Context context, int textviewid, int viewDesc, List<RSSItem> objects) {
                    super(context, textviewid, viewDesc, objects);
                    
                    this.objects = objects;
            }
            
            @Override
            public int getCount() {
                    return ((null != objects) ? objects.size() : 0);
            }
            
            @Override
            public long getItemId(int position) {
                    return position;
            }
            
            @Override
            public RSSItem getItem(int position) {
                    return ((null != objects) ? objects.get(position) : null);
            }
            
            public View getView(int position, View convertView, ViewGroup parent) {
                    View view = convertView;
                    
                    if(null == view)
                    {
                            LayoutInflater vi = (LayoutInflater)main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            view = vi.inflate(R.layout.rssitemview, null);
                    }
                    
                    RSSItem data = objects.get(position);
                    
                    if(null != data)
                    {
                            TextView title = (TextView)view.findViewById(R.id.txtTitle);
                        //    TextView date = (TextView)view.findViewById(R.id.txtDate);
                          //  TextView description = (TextView)view.findViewById(R.id.txtDescription);
                            
                            title.setText(data.title);
                            //date.setText("on " + data.date);
                            //description.setText(data.description);
                    }
                    
                    return view;
            }




            @SuppressWarnings("unused")
			public View getView2(final int position, final View convertView, final ViewGroup parent) {
                View view = convertView;
                
                if(null == view)
                {
                        LayoutInflater vi = (LayoutInflater)main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = vi.inflate(R.layout.rssitemview2, null);
                }
                
                RSSItem data = objects.get(position);
                
                if(null != data)
                {
                        TextView title = (TextView)view.findViewById(R.id.txtTitle);
                        TextView date = (TextView)view.findViewById(R.id.txtDate);
                        TextView description = (TextView)view.findViewById(R.id.txtDescription);
                        Button viewWeb = (Button)view.findViewById(R.id.Button01);
                        Intent intent = null;
                        
                        title.setText(data.title);
                        date.setText("on " + data.date);
                        description.setText(data.description);
                        intent = new Intent(Intent.ACTION_VIEW,
            					Uri.parse(data.link));
                }
                
                return view;
        }



}
}