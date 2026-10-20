import api from '../api';

// TODO: replace with the real backend endpoints
const ENDPOINTS = {
  company: '/items/company',
  saved: '/items/saved',
};

export function getUserItems({ type, page = 0, size = 20 } = {}) {
  const url = ENDPOINTS[type];
  if (!url) return Promise.reject(new Error(`Unknown items type: ${type}`));
  return api.get(url, { params: { page, size } }).then((res) => res.data);
}
