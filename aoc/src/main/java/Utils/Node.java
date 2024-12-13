package Utils;

class Node {
    long value;
    Node left;
    Node right;

    Node(long value) {
        this.value = value;

        right = null;
        left = null;
    }

    @Override
    public String toString() {
        Long leftValue = left == null ? null : left.value;
        Long rightValue = right == null ? null : right.value;
        return value + "Utils.Node(" + leftValue + " " + rightValue + ")";
    }
}
