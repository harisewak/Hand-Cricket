package com.harisewak.handcricket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harisewak.handcricket.R
import com.harisewak.handcricket.databinding.ActivityHandCricketBinding
import com.harisewak.handcricket.databinding.ItemEventBinding
import com.harisewak.handcricket.domain.PromptType
import com.harisewak.handcricket.presenter.HandCricketPresenter
import com.harisewak.handcricket.util.hide
import com.harisewak.handcricket.util.show
import java.util.ArrayList

class HandCricketActivity : AppCompatActivity(), HandCricketPresenter.View {

    private val presenter = HandCricketPresenter
    private lateinit var binding: ActivityHandCricketBinding
    private lateinit var eventListAdapter: EventListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHandCricketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()

        initAdapter()

        presenter.start(this)

    }

    private fun initAdapter() =
        binding.rvEvents.apply {
            eventListAdapter = EventListAdapter()
            adapter = eventListAdapter
        }


    private fun setListeners() {
        binding.btYes.setOnClickListener {
            when (presenter.promptType) {
                PromptType.CHOOSE_ODD_EVEN -> presenter.userSelectedOdd()
                PromptType.CHOOSE_BAT_BOWL -> presenter.userSelectedBatting()
                PromptType.PLAY_AGAIN -> presenter.restart()
            }
        }
        binding.btNo.setOnClickListener {
            when (presenter.promptType) {
                PromptType.CHOOSE_ODD_EVEN -> presenter.userSelectedEven()
                PromptType.CHOOSE_BAT_BOWL -> presenter.userSelectedBowling()
                PromptType.PLAY_AGAIN -> presenter.stop()
            }
        }
        binding.btOne.setOnClickListener {
            presenter.userSelectedNumber(1)
        }
        binding.btTwo.setOnClickListener {
            presenter.userSelectedNumber(2)
        }
        binding.btThree.setOnClickListener {
            presenter.userSelectedNumber(3)
        }
        binding.btFour.setOnClickListener {
            presenter.userSelectedNumber(4)
        }
        binding.btFive.setOnClickListener {
            presenter.userSelectedNumber(5)
        }
        binding.btSix.setOnClickListener {
            presenter.userSelectedNumber(6)
        }
    }

    // start of the game
    override fun showOddOrEvenPrompt() {
        binding.tvScore.text = ""
        eventListAdapter.clear()
        binding.tvPrompt.show()
        binding.tvPrompt.text = getString(R.string.text_choose_odd_even)
        showOddEvenButtons()
    }

    override fun shutdown() {
        finish()
    }

    override fun showSelectNumberForTossPrompt() {
        binding.tvPrompt.show()
        binding.tvPrompt.text = getString(R.string.text_select_toss_num)
        showNumPad()
    }

    private fun showNumPad() {
        binding.llNumberBtns.show()
        binding.llDecisionBtns.hide()
    }

    override fun showBatOrBowlPrompt() {
        binding.tvPrompt.show()
        binding.tvPrompt.text = getString(R.string.text_select_bat_or_bowl)
        showBatBowlButtons()
    }

    override fun showSelectNumberForPlayingPrompt() {
        binding.tvPrompt.show()
        binding.tvPrompt.text = getString(R.string.text_select_play_num)
        showNumPad()
    }

    override fun updateScore(score: String) {
        binding.tvScore.text = score
    }

    override fun showPlayAgainPrompt() {
        binding.tvPrompt.show()
        binding.tvPrompt.text = getString(R.string.text_play_again)
        showPlayAgainButtons()
    }

    override fun updateEventRecord(event: String) {
        eventListAdapter.add(event)
        val lastPos = eventListAdapter.itemCount - 1
        binding.rvEvents.scrollToPosition(lastPos)
    }

    private fun showOddEvenButtons() {
        binding.btYes.text = getString(R.string.text_btn_odd)
        binding.btNo.text = getString(R.string.text_btn_even)
        binding.llDecisionBtns.show()
        binding.llNumberBtns.hide()
    }

    private fun showBatBowlButtons() {
        binding.btYes.text = getString(R.string.text_btn_bat)
        binding.btNo.text = getString(R.string.text_btn_bowl)
        binding.llDecisionBtns.show()
        binding.llNumberBtns.hide()
    }

    private fun showPlayAgainButtons() {
        binding.btYes.text = getString(R.string.text_btn_yes)
        binding.btNo.text = getString(R.string.text_btn_no)
        binding.llDecisionBtns.show()
        binding.llNumberBtns.hide()
    }


    inner class EventListAdapter : RecyclerView.Adapter<EventListAdapter.EventListViewHolder>() {
        private val eventList = arrayListOf<String>()

        fun clear() {
            eventList.clear()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EventListAdapter.EventListViewHolder {
            val binding = ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return EventListViewHolder(binding)
        }

        override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
            val content = eventList[position]
            holder.bind(content)
        }

        override fun getItemCount() = eventList.size

        fun add(event: String) {

            // maintain only 5 recent events
            if (eventList.size == 5) {
                eventList.removeAt(0)
                notifyItemRemoved(0)
            }

            eventList.add(event)
            val lastPos = eventList.size - 1
            notifyItemInserted(lastPos)
        }


        inner class EventListViewHolder(private val binding: ItemEventBinding) :
            RecyclerView.ViewHolder(
                binding.root
            ) {

            fun bind(content: String) {
                binding.root.text = content
            }

        }
    }

}