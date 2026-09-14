import axios from 'axios';

const GATEWAY_ORIGIN = 'http://localhost:8081';
const LOGIN_URL = `${GATEWAY_ORIGIN}/oauth2/authorization/gateway-client`;

const api = axios.create({
  baseURL: `${GATEWAY_ORIGIN}/api`,
  withCredentials: true, // ensures the browser sends the SESSION cookie
});

// The gateway can't complete an OAuth2 login redirect inside an XHR call
// (it would hit the authorization server cross-origin and get blocked by CORS),
// so it returns 401 for unauthenticated /api calls instead. When that happens,
// send the browser on a real navigation to the gateway's login endpoint so the
// OAuth2 redirect flow runs as a normal page load.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      window.location.href = LOGIN_URL;
      return new Promise(() => {}); // navigation is in flight; never resolve
    }
    return Promise.reject(error);
  }
);

export default api;