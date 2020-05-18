package ahmedt.buruhid.ui.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ahmedt.buruhid.R;
import ahmedt.buruhid.ui.order.modelHistoryOrder.DataItem;
import ahmedt.buruhid.utils.UrlServer;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public HistoryOrderAdapter(Context context, ArrayList<DataItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<DataItem> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderAdapter.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
         final DataItem item = getItem(position);
         ViewHolder genericViewHolder = (ViewHolder) holder;
         String time = "";
         String type = "";
            if (item.getAnggota().matches("1")){
                type = "Individu worker";
            }else{
                type = "Team worker : "+item.getAnggota()+" people";
            }

            if (item.getFinishDate() != null){
                time = item.getFinishDate();
            }else{
                time = item.getOrderDate();
            }

          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Date date = format.parse(time);
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
                String realTime = format1.format(date);
                genericViewHolder.txtDate.setText(realTime);
            }catch(Exception e){
                Log.d("ASD", "onBindViewHolder: "+e.getMessage());
            }
         genericViewHolder.txtType.setText(type);
         genericViewHolder.txtDesc.setText(item.getJobdesk());

            if (item.getFoto().isEmpty()){
                Glide.with(context)
                        .load(R.drawable.blank_profile)
                        .apply(new RequestOptions().override(120,120))
                        .into(genericViewHolder.imgHistoryOrder);
            }else {
                Glide.with(context)
                        .load(UrlServer.URL_FOTO_TUKANG+item.getFoto())
                        .apply(new RequestOptions().override(120,120))
                        .into(genericViewHolder.imgHistoryOrder);
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private DataItem getItem(int position){
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, DataItem model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtType, txtDesc, txtDate, txtPrice;
        private ImageView imgHistoryOrder;
        private RatingBar ratingBar;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.txt_title_history_order);
            this.txtDesc = (TextView) itemView.findViewById(R.id.txt_desc_history_order);
            this.txtDate = (TextView) itemView.findViewById(R.id.txt_date_history_order);
            this.txtPrice = (TextView) itemView.findViewById(R.id.txt_price_history_order);
            this.imgHistoryOrder = (ImageView) itemView.findViewById(R.id.img_history_order);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }
    }
}