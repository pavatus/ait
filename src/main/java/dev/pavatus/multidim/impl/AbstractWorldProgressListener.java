package dev.pavatus.multidim.impl;

import net.minecraft.text.Text;
import net.minecraft.util.ProgressListener;

public class AbstractWorldProgressListener implements ProgressListener {

    @Override
    public void setTitle(Text title) { }

    @Override
    public void setTitleAndTask(Text title) { }

    @Override
    public void setTask(Text task) { }

    @Override
    public void progressStagePercentage(int percentage) { }

    @Override
    public void setDone() { }
}
