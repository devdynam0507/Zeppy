package kr.ndy.core.blockchain.observer;

import kr.ndy.core.blockchain.BlockHeader;

public interface IBlockObserver {

    void onGenerateBlock(BlockHeader header);
    void onFinishedPOW(BlockHeader header);

}
