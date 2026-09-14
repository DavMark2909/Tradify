import { createContext, useContext, useEffect, useState } from 'react';
import api from '../api';

const AuthContext = createContext(null);
const GATEWAY_AUTH_URL = 'http://localhost:8081/oauth2/authorization/gateway-client';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [status, setStatus] = useState('pending'); // 'pending' | 'authenticated' | 'error'

  useEffect(() => {
    let isMounted = true;
    api.get('/user/me')
      .then((res) => {
        if (!isMounted) return;
        setUser(res.data);
        setStatus('authenticated');
      })
      .catch((err) => {
        if (!isMounted) return;
        if (err.response?.status === 401) {
          window.location.href = GATEWAY_AUTH_URL;
        } else {
          setStatus('error');
        }
      });
    return () => { isMounted = false; };
  }, []);

  if (status === 'pending') {
    return <div>Loading...</div>;
  }
  if (status === 'error') {
    return <div>Something went wrong loading your account. Please try again later.</div>;
  }

  return <AuthContext.Provider value={{ user }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return ctx;
}