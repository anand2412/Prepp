package com.prepp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prepp.model.BranchDetail;
import com.prepp.R;

import java.util.List;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 9/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class BranchListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Activity mActivity;
    private List<BranchDetail> mBranchListData;
    public int mSelectedPosition=-1;

    public BranchListAdapter(Activity activity, List<BranchDetail> branchListData){
        mActivity=activity;
        mBranchListData=branchListData;
        mInflater=(LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mBranchListData.size();
    }

    @Override
    public BranchDetail getItem(int i) {
        return mBranchListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.branch_row, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_branch_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(getItem(position).getName());
        if(mSelectedPosition==position){
            holder.txtTitle.setTextColor(mActivity.getResources().getColor(R.color.violet));
            holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_selected,0,0,0);
        }else{
            holder.txtTitle.setTextColor(mActivity.getResources().getColor(R.color.blue_black));
            holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }

        return convertView;
    }

   /* @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = mBranchListData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            selectedPosition = filteredData.indexOf(selectedBranch);
            notifyDataSetChanged();
        }

    }
*/
    private class ViewHolder {
        TextView txtTitle;
    }
}
