import { useEffect, useState } from 'react';
import { getUserItems } from '../services/loadItems';

export function useItems(type) {
  const [items, setItems] = useState([]);
  const [status, setStatus] = useState('loading');

  useEffect(() => {
    let isMounted = true;
    setStatus('loading');
    getUserItems({ type, page: 0, size: 20 })
      .then((data) => {
        if (!isMounted) return;
        const content = data?.content ?? [];
        // saved items carry addedAt instead of createdAt (same as on Home)
        setItems(
          type === 'saved'
            ? content.map((item) => ({ ...item, createdAt: item.addedAt }))
            : content,
        );
        setStatus('success');
      })
      .catch(() => {
        if (!isMounted) return;
        setStatus('error');
      });
    return () => { isMounted = false; };
  }, [type]);

  return { items, setItems, status };
}
