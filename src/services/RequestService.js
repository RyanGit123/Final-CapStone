import axios from "axios";

/**
 * A module that provides methods for making requests related to maintenance requests and properties.
 * @module RequestService
 */
export default {    
    /**
     * Makes a new maintenance request.
     *
     * @param {Object} request - The maintenance request object.
     * @returns {Promise} A promise that resolves with the response from the server.
     */
    makeRequest(request) {
        return axios.post("/maintenance_requests", request);
    },
    /**
     * Retrieves maintenance requests by property ID.
     *
     * @param {number} id - The property ID.
     * @returns {Promise} A promise that resolves with the response from the server.
     */
    getRequestsByPropertyId(id) {
        return axios.get(`/maintenance_requests/properties/${id}`);
    },
    /**
     * Retrieves maintenance requests by worker ID.
     *
     * @param {number} id - The worker ID.
     * @returns {Promise} A promise that resolves with the response from the server.
     */
    getRequestsByWorkerId(id){
        return axios.get(`/maintenance_requests/workers/${id}`);
    },
    /**
     * Marks a maintenance request as complete.
     *
     * @param {number} id - The maintenance request ID.
     * @returns {Promise} A promise that resolves with the response from the server.
     */
    markComplete(id){
        return axios.put(`/maintenance_requests/${id}/complete`);
    },
    /**
     * Assigns a worker to a maintenance request.
     *
     * @param {number} id - The maintenance request ID.
     * @param {number} workerId - The worker ID.
     * @returns {Promise} A promise that resolves with the response from the server.
     */
    assignWorker(id, workerId){
        return axios.put(`/maintenance_requests/${id}/assign/${workerId}`);
    },

    getAllRequests(){
        return axios.get(`/maintenance_requests`);
    }
   
    
};