import api from '../api';

// TODO: replace with the real backend endpoint and payload shape
const CREATE_ITEM_ENDPOINT = '/product/create';

export function createItem(item) {
  return api.post(CREATE_ITEM_ENDPOINT, item).then((res) => res.data);
}
