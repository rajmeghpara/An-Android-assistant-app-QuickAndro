package com.rarity.apps.quickandro;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.rarity.apps.quickandro.Modules.Calculator;
import com.rarity.apps.quickandro.Modules.Call;
import com.rarity.apps.quickandro.Modules.Contacts;
import com.rarity.apps.quickandro.Modules.Message;
import com.rarity.apps.quickandro.Modules.OpenApp;
import com.rarity.apps.quickandro.Modules.ProfileManager;
import com.rarity.apps.quickandro.Modules.Search;
import com.rarity.apps.quickandro.Modules.SetAlarm;
import com.rarity.apps.quickandro.Modules.SpeakText;
import com.rarity.apps.quickandro.Modules.Switch;
import com.rarity.apps.quickandro.Modules.Temp_SpeechToText;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements RunBot//, RecognitionListener
{
    /*variables used for layout*/
    ImageButton btn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Fragment f1 = new OneFragment();
    Fragment f2 = new SecondFragment();
    ArrayList<String> conversation = new ArrayList<String>();
    private ListView drawerList;
    private TextView email;
    private AdView adBanner;
    private InterstitialAd iad;
    private AdRequest adReBan;
    private AdRequest adReInt;
    private Dialog alet_box;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;


    /*most commonly used modules*/
//    private SpeechToText stt;
    private Temp_SpeechToText stt;
    private SpeakText tts;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager=new PrefManager(this);
        if(prefManager.getMessage()!=null) {
            String[] msg = prefManager.getMessage().split(";,;");
            for (int i = 0; i < msg.length; i++) {
                conversation.add(msg[i]);
            }
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        drawerList = (ListView) findViewById(R.id.drawerlist);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open_drawer,R.string.close_drawer);
        toolbar.setNavigationIcon(R.drawable.toast_image);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        // To apply color in toolbar's default icon
        toolbar.setNavigationIcon(R.drawable.toast_image);
        int color = getResources().getColor(R.color.colorPrimaryDark);
        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        toolbar.getOverflowIcon().setColorFilter(colorFilter);


        // toolbar.getNavigationIcon().setColorFilter(colorFilter);

        if(savedInstanceState != null)
            conversation = savedInstanceState.getStringArrayList("conversation");

        startService(new Intent(this, RunBackground.class));

        /*initializing global variables*/

        btn = (ImageButton) findViewById(R.id.button);
//        stt = new SpeechToText(this, this);
        stt = new Temp_SpeechToText(this);
        tts = new SpeakText(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((OneFragment)f1).command.getText().toString().equals("")) {
                    run();
                }
                else{
                    tts.stopSpeaking();
                    updateLayout(" "+((OneFragment)f1).command.getText().toString() );
                    callModule( ((OneFragment)f1).command.getText().toString() );
                    ((OneFragment)f1).command.setText("");
                }
            }
        });

        drawerList.setAdapter(new DrawerListAdapter(this));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, final int i, long l) {

                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                alet_box = new Dialog(MainActivity.this);
                final View view = inflater.inflate(R.layout.custom_dialog, null, false);
                final TextView title= (TextView) view.findViewById(R.id .title);
                final TextView description= (TextView) view.findViewById(R.id.description);
                final TextView bt = (Button) view.findViewById(R.id.bt);
                final String arr[]={"Call Stark","Calculate 159 + 289","Open QuickAndro","Message Person","Profile Silent","Search Dictionary Android","Turn On Flash","Alarm 6:45 A.M."};
                alet_box.requestWindowFeature(Window.FEATURE_NO_TITLE);

                String data[] = (String []) adapterView.getAdapter().getItem(i);
                title.setText(data[0]);
                description.setText(data[1]);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(view)
                        .show();

                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tts.speak(arr[i]);
                    }
                });
            }
        });

//        String android_id = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        adBanner = (AdView) findViewById(R.id.adBanner);
        adReBan = new AdRequest.Builder().build();
        adBanner.loadAd(adReBan);

        adBanner.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
        //Toast.makeText(this,android_id,Toast.LENGTH_LONG).show();

        interStitial();
    }

    public void interStitial(){
        iad = new InterstitialAd(this);
        iad.setAdUnitId("ca-app-pub-7145982136829866/6592822232");
//        String android_id = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        adReInt = new AdRequest.Builder().build();
        iad.loadAd(adReInt);
        //  Toast.makeText(MainActivity.this, "request started", Toast.LENGTH_SHORT).show();
    }
    public void showInterstitial()
    {
        if(iad.isLoaded())
        {
            iad.show();
        }
        else{
            this.finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("conversation", conversation);
    }

    @Override
    public void onPause() {
        if (adBanner != null) {
           adBanner.pause();
       }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adBanner != null) {
            adBanner.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adBanner != null) {
            adBanner.destroy();
        }
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<conversation.size();i++){
            sb.append(conversation.get(i)).append(";,;");
        }
        prefManager.setMessage(sb.toString());


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // retrieves data from the VoiceRecognizer
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            updateLayout(" " + result);
            callModule(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        Intent intent = new Intent();
        switch (id){
            case R.id.share:intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link));
                intent.putExtra(Intent.EXTRA_SUBJECT, "QuickAndro app");
                startActivity(Intent.createChooser(intent, null));
                break;
            case R.id.about:intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rate:intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.rarity.apps.quickandro"));
                startActivity(intent);
                break;
            case R.id.exit:
                System.exit(0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void run(){
        //showCustomToast();
        tts.stopSpeaking();
        stt.listen();
    }

    private void showCustomToast(){
        LayoutInflater inflater = getLayoutInflater();
        View viewOfToast = inflater.inflate(R.layout.custom_toast, //layout file to use
                (ViewGroup)findViewById(R.id.toast_layout)); //root layout id(can use null too)
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(viewOfToast);
        toast.show();
    }

    /*method to add new message in layout*/
    @Override
    public void updateLayout(String message){
        ((SecondFragment)f2).updateListView(message);
        if(message.charAt(0)!=' ')    //this will speak the message aloud if that is written by the bot.
            tts.speak(message);
    }

    /*this method will decide which function of app to call based on user's command*/
    private void callModule(String message){
        String temp[] = message.split(" ", 2);

        if(temp.length < 2){
            tts.speak("Invalid command, try again");
            updateLayout("Invalid command, try again");
            return;
        }

        String command = temp[0];     //stores the first word(ie. command)
        String argument = temp[1].toLowerCase();

        /*call functions based on command*/
        if(command.equalsIgnoreCase("call")){
            commandCall(argument);
        }
        else if(command.equalsIgnoreCase("calculate")){
            commandCalculate(argument);
        }
        else if(command.equalsIgnoreCase("message")){
            commandMessage(argument);
        }
        else if(command.equalsIgnoreCase("open")){
            commandOpen(argument);
        }
        else if(command.equalsIgnoreCase("profile")){
            commandProfile(argument);
        }
        else if(command.equalsIgnoreCase("search")){
            commandSearch(argument);
        }
        else if(command.equalsIgnoreCase("turn")){
            commandTurn(argument);
        }
        else if(command.equalsIgnoreCase("alarm")){
            commandAlarm(argument);
            Toast.makeText(this, argument, Toast.LENGTH_LONG).show();
        }
        else if(command.equalsIgnoreCase("close")){
            finish();
            System.exit(0);
        }
        else{
            tts.speak("Invalid command, try again");
            updateLayout("Invalid command, try again");
        }
    }

    //this method will be called when the result of SpeechToText module got decleared
//    @Override
//    public void onResults(Bundle resultBundle) {
//        String result = resultBundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
//        updateLayout(" " + result);
//        callModule(result);
//    }
//
//    @Override
//    public void onReadyForSpeech(Bundle params) {}
//    @Override
//    public void onError(int error) {}
//    @Override
//    public void onBeginningOfSpeech() {}
//    @Override
//    public void onBufferReceived(byte[] buffer) {}
//    @Override
//    public void onEndOfSpeech() {}
//    @Override
//    public void onEvent(int eventType, Bundle params) {}
//    @Override
//    public void onPartialResults(Bundle partialResults) {}
//    @Override
//    public void onRmsChanged(float rmsdB) {}

    /*function to call a person*/
    private void commandCall(String argument){
        Call call = new Call(this);
        Contacts contacts = new Contacts(this);
        String reply;

        if(argument.length()==0) {
            reply = "Please try again with the name or number of person.";
        }
        else {
            try {
                String temp = argument.replaceAll(" ", "");
                Long.parseLong(temp);
                reply = call.call(temp);
            }
            catch (NumberFormatException e) {
                if (contacts.findNumber(argument) == null) {
                    ((OneFragment) f1).setSuggest(contacts.getFinallist());
                    ((OneFragment) f1).setnumberList(contacts.getnumberlist());

                    if (contacts.getFinallist().size() != 0)
                        reply = "multiple contacts found, please select one";
                    else {
                        reply = "sorry, could not found the number of " + argument;
                    }
                } else
                    reply = call.call(contacts.findNumber(argument));
            }
        }

        if(reply.compareToIgnoreCase("calling") == 0)
            reply += " " + argument;

        tts.speak(reply);
        updateLayout(reply);
    }

    /*function to calculate a mathematical expression*/
    private void commandCalculate(String argument){
        Calculator calculator = new Calculator(this);

        if(argument.equals(""))
            calculator.calculate();
        else
            updateLayout(calculator.calculate(argument));
    }

    /*function to send a message*/
    private void commandMessage(String argument){
        Message msg = new Message(this);

        Toast.makeText(this, "."+argument+".", Toast.LENGTH_SHORT).show();

        if(argument.equals(""))
            msg.sendMessage();
        else
            msg.sendMessage(argument);
    }

    /*function to open a app*/
    private void commandOpen(String argument){
        OpenApp openApp = new OpenApp(this);
        updateLayout(openApp.openApp(argument));
    }

    /*function to change prpofile*/
    private void commandProfile(String argument){
        ProfileManager profileManager = new ProfileManager(this);
        updateLayout(profileManager.changeProfile(argument));
    }

    /*function for search*/
    private void commandSearch(String argument) {
        Search search = new Search(this);
        if(argument.split(" ")[0].equalsIgnoreCase("wiki"))
            updateLayout(search.wikiSearch(argument.replaceFirst("wiki ", "")));
        else if(argument.split(" ")[0].equalsIgnoreCase("wikipedia"))
            updateLayout( search.wikiSearch(argument.replaceFirst("wikipedia ", "")) );
        else if(argument.split(" ")[0].equalsIgnoreCase("dictionary"))
            updateLayout( search.dictionarySearch(argument.replaceFirst("dictionary ", "")) );
        else if(argument.split(" ")[0].equalsIgnoreCase("youtube"))
            updateLayout( search.youtubeSearch(argument.replaceFirst("youtube ", "")) );
        else
            updateLayout( search.googleSearch(argument) );
    }

    /*function for extra utilities*/
    private void commandTurn(String argument){
        Switch s = new Switch(this);
        updateLayout( s.utility(argument) );
    }

    /*function for alarm*/
    private void commandAlarm(String argument){
        SetAlarm alarm = new SetAlarm(this);
        updateLayout( alarm.setAlarm(argument) );
    }

    //last update
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(f1, "MAIN");
        adapter.addFragment(f2, "HISTORY");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

       super.onBackPressed();
        showInterstitial();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}