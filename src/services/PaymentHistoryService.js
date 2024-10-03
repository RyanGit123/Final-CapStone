import axios from "axios";

export default {    
getPayhistoryByPropertyId(id) {
    return axios.get(`/payment_history/properties/${id}`);

},
getPayHistoryByUserId(id){
    return axios.get(`/payment_history/user/${id}`); 

}, 
makePayment(payment){
    return axios.post("/payment_history", payment);
}
};