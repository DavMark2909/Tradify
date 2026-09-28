import api from '../api';

export function getSectors() {
  return api.get('/sector').then((res) => res.data);
}
