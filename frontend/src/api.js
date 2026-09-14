import axios from 'axios';

const GATEWAY_ORIGIN = 'http://localhost:8081';
const LOGIN_URL = `${GATEWAY_ORIGIN}/oauth2/authorization/gateway-client`;

const api = axios.create({
  baseURL: `${GATEWAY_ORIGIN}/api`,
  withCredentials: true, // ensures the browser sends the SESSION cookie
});


api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      window.location.href = LOGIN_URL;
      return new Promise(() => {}); 
    }
    return Promise.reject(error);
  }
);

export default api;