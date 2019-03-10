package com.example.admin.inzv2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 2016-08-23.
 */
public class Patient_FillPoling_Adapter extends RecyclerView.Adapter<Patient_FillPoling_Adapter.ViewHolder> {

    private int value;
    public String[] anserwsarray ;
    public String editedvalue;
    public ProgressDialog progressDialog;
    private DoctorModel doctor;
    private PatientModel patient;

    // Provide a suitable constructor (depends on the kind of dataset)
    public Patient_FillPoling_Adapter(int value) {

        this.value= value;
        anserwsarray = new String[Patient_DoctorList_Activity.doctorModelsList.get(value).getQuestions().size()];
        doctor=Patient_DoctorList_Activity.doctorModelsList.get(value);
        patient=Patient_Menu_Activity.getPatient();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class PatientQuestionViewHolder extends ViewHolder {
        public TextView questionline;
        public EditText anserwline;


        public PatientQuestionViewHolder(View itemView) {
            super(itemView);
            this.questionline = (TextView) itemView.findViewById(R.id.questionlinepolling);
            this.anserwline= (EditText) itemView.findViewById(R.id.anserweditText);

        }
    }

    public class PatientPolingViewHolder extends ViewHolder{

        public TextView questionline;
        public EditText anserwline;
        public Button btnButton1;

        public PatientPolingViewHolder(View itemView) {
            super(itemView);
            this.questionline = (TextView) itemView.findViewById(R.id.questionlinepolling);
            this.anserwline= (EditText) itemView.findViewById(R.id.anserweditText);
            this.btnButton1 = (Button) itemView.findViewById(R.id.patientsendpollingbutton);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        if (getItemCount()-1==viewType) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.patient_fillpolling_card, viewGroup, false);
            return new PatientPolingViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.patient_polling_card, viewGroup, false);
            return new PatientQuestionViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (viewHolder.getItemViewType() == getItemCount() - 1) {
            final PatientPolingViewHolder holder = (PatientPolingViewHolder) viewHolder;
            holder.questionline.setText(doctor.getQuestions().get(position).question);
            holder.anserwline.setTag(false);
            holder.anserwline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    holder.anserwline.setTag(true);
                }
            });
            holder.anserwline.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("") && (Boolean) holder.anserwline.getTag()) {
                        // Do your Logic here
                        editedvalue = holder.anserwline.getText().toString();
                        anserwsarray[position] = holder.anserwline.getText().toString();
                    }
                }
            });

            holder.btnButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    /// button click event
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Wysyłanie Ankiety")
                            .setMessage("Czy na pewno chcesz wysłać ankietę?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                    buttonsendaction();
                                    progressDialog = new ProgressDialog(v.getContext());
                                    String url = "http://boguinzynierkarestapi.azurewebsites.net/api/pacient/" + String.valueOf(patient.getId());
                                    new PutDataTask().execute(url);

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });


        }
            else{
            final PatientQuestionViewHolder holder = (PatientQuestionViewHolder) viewHolder;
            holder.questionline.setText(doctor.getQuestions().get(position).question);
            holder.anserwline.setTag(false);
            holder.anserwline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    holder.anserwline.setTag(true);
                }
            });
            holder.anserwline.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals("") && (Boolean) holder.anserwline.getTag()) {
                        // Do your Logic here
                        editedvalue = holder.anserwline.getText().toString();
                        anserwsarray[position] = holder.anserwline.getText().toString();
                    }
                }
            });
            }
        }






    private void buttonsendaction() {
        addPollingtoPatient();
    }

    private void addPollingtoPatient() {
        String doctorname= doctor.getDoctorname();
        String doctoremail= doctor.getEmail();
        Question.List questionList=new Question.List() ;
        for (int i=0; i<getItemCount();i++ ) {
            Question q = new Question(doctor.getQuestions().get(i).question, anserwsarray[i]);
            questionList.add(q);
        }
        DateTime created_at=DateTime.now();
        Diagnosis.List diagnosisList=new Diagnosis.List();
        Diagnosis d =new Diagnosis("Oczekiwanie na odpowiedź lekarza...",created_at);
        diagnosisList.add(d);

        patient.getPolingList().add(new Poling(questionList, doctorname, doctoremail, diagnosisList, created_at));

        doctor.getPatients().add(new Patient2(Patient_Menu_Activity.getPatient().getId(), created_at));
    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return doctor.getQuestions().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class PutDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

      //       Context context = null;
      //      progressDialog = new ProgressDialog(context.getApplicationContext());
            progressDialog.setMessage("Ladowanie danych...");
            progressDialog.show();

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("Wysłanie ankiety się powiodło")) {
                String url = "http://boguinzynierkarestapi.azurewebsites.net/api/status/" + String.valueOf(doctor.getId());
                new PutDataTask2().execute(url);
            }
            else
                Toast.makeText(progressDialog.getContext(), result, Toast.LENGTH_LONG).show();
            /*
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            */
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return putData(params[0]);
            } catch (IOException e) {
                return "Brak dostępu do internetu";
            } catch (JSONException e) {
                return "Złe dane";
            }

        }


        private String putData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            String result = null;
            //create data to update
            JSONObject dataToSend = patient.patienttoJsonPolling(patient);

            //Initialize and config request, then connect to server.
            try {
                //initialize and config request then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //check udate succesfull or not
                if (urlConnection.getResponseCode() == 200) {
                    return "Wysłanie ankiety się powiodło";
                } else {

                    return "Wysłanie ankiety się nie powiodło";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }
            }

        }
    }

    public class PutDataTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            Toast.makeText(progressDialog.getContext(), result.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return putData(params[0]);
            } catch (IOException e) {
                return "Brak dostępu do internetu";
            } catch (JSONException e) {
                return "Złe dane";
            }

        }


        private String putData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            String result = null;
            //create data to update
            JSONObject dataToSend = doctor.DoctortoJsonPatientid(doctor);
            //Initialize and config request, then connect to server.
            try {
                //initialize and config request then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(25000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //check udate succesfull or not
                if (urlConnection.getResponseCode() == 200) {
                    return "Wysłanie ankiety się powiodło";
                } else {

                    return "Wysłanie ankiety się nie powiodło";
                }
            } finally {
                if (bufferedWriter != null) {

                    bufferedWriter.close();
                }


            }

        }
    }

}


