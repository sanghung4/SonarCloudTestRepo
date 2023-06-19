type EventKey<T extends Events> = string & keyof T;
type Listener<T> = (e: T) => void;
type Events = Record<string, any>;

export class EventEmitter<T extends Events> {
  events: {
    [K in keyof Events]?: ((e: Events[K]) => void)[];
  } = {};

  addListener<K extends EventKey<T>>(name: K, fn: Listener<T[K]>): void {
    this.events[name] = (this.events[name] || []).concat(fn);
  }

  removeListener<K extends EventKey<T>>(name: K, fn: Listener<T[K]>): void {
    this.events[name] = (this.events[name] || []).filter((e) => e !== fn);
  }

  removeAllListeners<K extends EventKey<T>>(name: K): void {
    delete this.events[name];
  }

  emit<K extends EventKey<T>>(name: K, data?: T[K]): void {
    (this.events[name] || []).forEach((cb) => cb(data));
  }
}
