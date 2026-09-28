import { useEffect, useState } from 'react';
import { getSectors } from '../../services/sectorService';
import StatusMessage from '../common/StatusMessage';
import './BuyerCategoryPanel.css';

export default function BuyerCategoryPanel({ onSelectionChange, className = '' }) {
  const [sectors, setSectors] = useState([]);
  const [status, setStatus] = useState('loading');
  const [selectedIds, setSelectedIds] = useState([]);

  useEffect(() => {
    let isMounted = true;
    getSectors()
      .then((data) => {
        if (!isMounted) return;
        setSectors(data);
        setStatus('success');
      })
      .catch(() => {
        if (!isMounted) return;
        setStatus('error');
      });
    return () => { isMounted = false; };
  }, []);

  const toggleSector = (id) => {
    setSelectedIds((prev) => {
      const next = prev.includes(id) ? prev.filter((sectorId) => sectorId !== id) : [...prev, id];
      onSelectionChange?.(next);
      return next;
    });
  };

  return (
    <div className={`buyer-category-panel ${className}`}>
      <h2 className="buyer-category-panel__title">Categories</h2>
      <div className="buyer-category-panel__list-wrap">
        {status === 'loading' && <StatusMessage tone="loading">Loading categories…</StatusMessage>}
        {status === 'error' && <StatusMessage tone="error">Couldn't load categories.</StatusMessage>}
        {status === 'success' && sectors.length === 0 && (
          <StatusMessage tone="empty">No categories available.</StatusMessage>
        )}
        {status === 'success' && sectors.length > 0 && (
          <ul className="buyer-category-panel__list">
            {sectors.map((sector) => (
              <li key={sector.id}>
                <label className="buyer-category-panel__item">
                  <input
                    type="checkbox"
                    checked={selectedIds.includes(sector.id)}
                    onChange={() => toggleSector(sector.id)}
                  />
                  <span>{sector.name}</span>
                </label>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
