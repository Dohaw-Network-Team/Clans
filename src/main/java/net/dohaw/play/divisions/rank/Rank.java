package net.dohaw.play.divisions.rank;

import java.util.Arrays;
import java.util.List;

public enum Rank {

    FRESH_MEAT,
    PROVEN_MEMBER,
    LOYAL,
    ELDER,
    OVERLORD;

    /*
        If rank1 is higher than rank2
        1 - Higher
        0 - Same
        -1 - Lower
     */

    /*
        Use #ordinal() next time
     */

    public static int isAHigherRank(Rank rank1, Rank rank2){
        List<Rank> ranks = list();
        if(ranks.indexOf(rank1) > ranks.indexOf(rank2)){
            return 1;
        }else if(ranks.indexOf(rank1) < ranks.indexOf(rank2)){
            return -1;
        }else if(ranks.indexOf(rank1) == ranks.indexOf(rank2)){
            return 0;
        }
        return 0;
    }

    public static Rank getNextRank(Rank currentRank){
        List<Rank> ranks = list();
        int indexCurrentRank = ranks.indexOf(currentRank);
        return ranks.get(indexCurrentRank + 1);
    }

    public static Rank getPreviousRank(Rank currentRank){
        List<Rank> ranks = list();
        int indexCurrentRank = ranks.indexOf(currentRank);
        return ranks.get(indexCurrentRank - 1);
    }

    public static boolean isLastRank(Rank rank){
        return list().get(list().size() - 1) == rank;
    }

    public static boolean isFirstRank(Rank rank){
        return list().get(0) == rank;
    }

    public static List<Rank> list(){
        return Arrays.asList(values());
    }

}
