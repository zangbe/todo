const getters = {
    getResultMsgObj() {
        return (args) => {
            let obj = {};
            obj.msg = args.msg;
            obj.color = args.color;

            return obj;
        }
    }
}

export default getters