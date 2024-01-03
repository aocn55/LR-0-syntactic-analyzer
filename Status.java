package Entity;

import java.util.List;
import java.util.Map;

public class Status {
        private int statusNum; // 状态编号

        private int itemType; /* 0:移进 1:待约 2:归约 3:接受 */
        private List<String> statusGrammar; // 该状态的文法

        Map<Character, Integer> statusMap; // 状态转换表

        public Status(int statusNum, List<String> statusGrammar, Map<Character, Integer> statusMap, int itemType) {
            this.statusNum = statusNum;
            this.statusGrammar = statusGrammar;
            this.statusMap = statusMap;
            this.itemType = itemType;
        }


        public Status() {
        }

        /**
         * 获取
         * @return statusNum
         */
        public int getStatusNum() {
            return statusNum;
        }

        /**
         * 设置
         */
        public void setStatusNum(int statusNum) {
            this.statusNum = statusNum;
        }

        /**
         * 获取
         * @return itemType
         */
        public int getItemType() {
            return itemType;
        }

        /**
         * 设置
         */
        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        /**
         * 获取
         * @return statusGrammar
         */
        public List<String> getStatusGrammar() {
            return statusGrammar;
        }

        /**
         * 设置
         */
        public void setStatusGrammar(List<String> statusGrammar) {
            this.statusGrammar = statusGrammar;
        }

        /**
         * 获取
         * @return statusMap
         */
        public Map<Character, Integer> getStatusMap() {
            return statusMap;
        }

        /**
         * 设置
         */
        public void setStatusMap(Map<Character, Integer> statusMap) {
            this.statusMap = statusMap;
        }

        public String toString() {
            return "Status{statusNum = " + statusNum + ", itemType = " + itemType + ", statusGrammar = " + statusGrammar + ", statusMap = " + statusMap + "}";
        }
}
