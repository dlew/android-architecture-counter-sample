package net.danlew.counter.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.counter.view.*
import net.danlew.counter.R
import net.danlew.counter.data.Counter
import net.danlew.counter.data.CounterChange

class CounterViewHolder(context: Context, parent: ViewGroup)
  : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.counter, parent, false)) {

  companion object {
    private val DUMMY_COUNTER = Counter(-1)
  }

  private var counter: Counter = DUMMY_COUNTER
  private lateinit var listener: Listener

  init {

    itemView.plus_button.setOnClickListener {
      listener.onCounterChange(CounterChange.Count(counter.id, 1))
    }

    itemView.minus_button.setOnClickListener {
      listener.onCounterChange(CounterChange.Count(counter.id, -1))
    }

    itemView.counter_name.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        // If you try to clear focus immediately it doesn't work
        v.postDelayed({ v.clearFocus() }, 0)
      }
      false
    }

    itemView.counter_name.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable)
          = listener.onCounterChange(CounterChange.Name(counter.id, s.toString())) ?: Unit

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })
  }

  fun bind(counter: Counter, listener: Listener) {
    if (this.counter == counter) {
      return
    }

    this.listener = listener
    this.counter = counter
    if (itemView.counter_name.text.toString() != counter.name) {
      itemView.counter_name.setText(counter.name)
    }
    itemView.count.text = counter.count.toString()
  }

  fun detach() {
    if (itemView.counter_name.hasFocus()) {
      itemView.counter_name.clearFocus()
    }
  }

  interface Listener {
    fun onCounterChange(counterChange: CounterChange)
  }
}