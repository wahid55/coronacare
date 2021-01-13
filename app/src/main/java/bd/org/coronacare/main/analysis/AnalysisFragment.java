package bd.org.coronacare.main.analysis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bd.org.coronacare.R;
import bd.org.coronacare.utils.DataPicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnalysisFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText fever;
    private TextInputEditText tiredness;
    private TextInputEditText dryCough;
    private TextInputEditText breathing         ;
    private TextInputEditText soreThroat;
    private TextInputEditText pains;
    private TextInputEditText nasalCongestion;
    private TextInputEditText runnyNose;
    private TextInputEditText diarrhea;
    private TextInputEditText contactWithPatient;
    private ExtendedFloatingActionButton chkBtn;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.fragment_analysis, container, false);
        fever = frame.findViewById(R.id.rska_fever);
        tiredness = frame.findViewById(R.id.rska_tiredness);
        dryCough = frame.findViewById(R.id.rska_dry_cough);
        breathing = frame.findViewById(R.id.rska_breathing);
        soreThroat = frame.findViewById(R.id.rska_sore_throat);
        pains = frame.findViewById(R.id.rska_pains);
        nasalCongestion = frame.findViewById(R.id.rska_nasal_congestion);
        runnyNose = frame.findViewById(R.id.rska_runny_nose);
        diarrhea = frame.findViewById(R.id.rska_diarrhea);
        contactWithPatient = frame.findViewById(R.id.rska_contact_with_patient);
        chkBtn = frame.findViewById(R.id.rska_btn);


        fever.setOnClickListener(this);
        tiredness.setOnClickListener(this);
        dryCough.setOnClickListener(this);
        breathing.setOnClickListener(this);
        soreThroat.setOnClickListener(this);
        pains.setOnClickListener(this);
        nasalCongestion.setOnClickListener(this);
        runnyNose.setOnClickListener(this);
        diarrhea.setOnClickListener(this);
        contactWithPatient.setOnClickListener(this);
        chkBtn.setOnClickListener(this);
        return frame;
    }

    private Integer processData(TextInputEditText field) {
        if (field.getText().equals("Yes")) {
            return 1;
        } else if (field.getText().equals("No")) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(fever)) {
            DataPicker.chooseAnOption(getActivity(), fever, new String[] {"Yes", "No"});
        } else if(v.equals(tiredness)) {
            DataPicker.chooseAnOption(getActivity(), tiredness, new String[] {"Yes", "No"});
        } else if(v.equals(dryCough)) {
            DataPicker.chooseAnOption(getActivity(), dryCough, new String[] {"Yes", "No"});
        } else if(v.equals(breathing)) {
            DataPicker.chooseAnOption(getActivity(), breathing, new String[] {"Yes", "No"});
        } else if(v.equals(soreThroat)) {
            DataPicker.chooseAnOption(getActivity(), soreThroat, new String[] {"Yes", "No"});
        } else if(v.equals(pains)) {
            DataPicker.chooseAnOption(getActivity(), pains, new String[] {"Yes", "No"});
        } else if(v.equals(nasalCongestion)) {
            DataPicker.chooseAnOption(getActivity(), nasalCongestion, new String[] {"Yes", "No"});
        } else if(v.equals(runnyNose)) {
            DataPicker.chooseAnOption(getActivity(), runnyNose, new String[] {"Yes", "No"});
        } else if(v.equals(diarrhea)) {
            DataPicker.chooseAnOption(getActivity(), diarrhea, new String[] {"Yes", "No"});
        } else if(v.equals(contactWithPatient)) {
            DataPicker.chooseAnOption(getActivity(), contactWithPatient, new String[] {"Yes", "No", "Don't know"});
        } else if(v.equals(chkBtn)) {
            if (TextUtils.isEmpty(fever.getText()) || TextUtils.isEmpty(tiredness.getText()) || TextUtils.isEmpty(dryCough.getText()) || TextUtils.isEmpty(breathing.getText()) || TextUtils.isEmpty(soreThroat.getText()) || TextUtils.isEmpty(pains.getText()) || TextUtils.isEmpty(nasalCongestion.getText()) || TextUtils.isEmpty(runnyNose.getText()) || TextUtils.isEmpty(diarrhea.getText()) || TextUtils.isEmpty(contactWithPatient.getText())) {
                Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                final ProgressDialog preLoader = new ProgressDialog(getActivity(), R.style.AppTheme_ProgressDialog);
                preLoader.setMessage("Analyzing your symptoms...");
                preLoader.setCanceledOnTouchOutside(false);
                preLoader.show();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                OkHttpClient client = builder.build();

                String url = "https://coronacare-diagnostic.herokuapp.com/?" + "fever=" + processData(fever) + "&tiredness=" + processData(tiredness) + "&dry_cough=" + processData(dryCough) + "&difficulty_in_breathing=" + processData(breathing) + "&sore_throat=" + processData(soreThroat) + "&pains=" + processData(pains) + "&nasal_congestion=" + processData(nasalCongestion) + "&runny_nose=" + processData(runnyNose) + "&diarrhea=" + processData(diarrhea) + "&contact_with_patient=" + processData(contactWithPatient);
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        preLoader.dismiss();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Failed to send request", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        preLoader.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject result = new JSONObject(response.body().string());
                                startActivity(new Intent(getActivity(), AnalysisResultActivity.class).putExtra("SEVERITY", result.getString("severity")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }
}