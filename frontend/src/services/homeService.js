import api from '../api';

export function getHomeView({ page = 0, size = 20 } = {}) {
  return api.get('/home/', { params: { page, size } }).then((res) => res.data);
}
