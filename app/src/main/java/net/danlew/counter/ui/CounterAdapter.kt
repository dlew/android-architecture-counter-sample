package net.danlew.counter.ui

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import net.danlew.counter.data.Counter
import net.danlew.counter.data.CounterChange

class CounterAdapter(private val context: Context) : RecyclerView.Adapter<CounterViewHolder>() {

  private var data: List<Counter> = emptyList()

  private val changes = PublishRelay.create<CounterChange>()

  private val listener = object : CounterViewHolder.Listener {
    override fun onCounterChange(counterChange: CounterChange) = changes.accept(counterChange)
  }

  init {
    setHasStableIds(true)
  }

  fun changes(): Observable<CounterChange> = changes.hide()

  fun bind(source: Flowable<List<Counter>>): Disposable {
    data class DataWithDiff(val counters: List<Counter>, val diff: DiffUtil.DiffResult?)

    return source
        .scan(DataWithDiff(emptyList(), null)) { (old), new ->
          DataWithDiff(new, DiffUtil.calculateDiff(DiffCallback(old, new)))
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          data = it.counters
          if (it.diff != null) {
            it.diff.dispatchUpdatesTo(this)
          }
          else {
            notifyDataSetChanged()
          }
        }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CounterViewHolder(context, parent)

  override fun onBindViewHolder(holder: CounterViewHolder, position: Int) = holder.bind(data[position], listener)

  override fun onViewDetachedFromWindow(holder: CounterViewHolder) = holder.detach()

  override fun getItemCount() = data.size

  override fun getItemId(position: Int) = data[position].id

  fun getPosition(itemId: Long) = data.indexOfFirst { it.id == itemId }

  private class DiffCallback(private val old: List<Counter>, private val new: List<Counter>) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
        = old[oldItemPosition].id == new[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int)
        = old[oldItemPosition] == new[newItemPosition]
  }
}